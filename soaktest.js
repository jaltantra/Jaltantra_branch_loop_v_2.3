import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  // 1) ramp up from 0 → 50 VUs over 5m
  // 2) hold at 50 VUs for 4h
  // 3) ramp down to 0 over 5m
  stages: [
    { duration: '5m', target: 50 },
    { duration: '1h', target: 50 },
    { duration: '5m', target: 0 },
  ],

  // send all metrics to InfluxDB for Grafana
  // you can also add: , { prefix: 'jaltantra_soak' } if you want metric prefixes
  // when you run: k6 run --out influxdb=http://localhost:8086/jaltantra_metrics soak_test.js
  // make sure InfluxDB is configured with database 'jaltantra_metrics'
  ext: {
    influxdb: {
      // this host/port is configured at run-time via CLI, so we leave host empty:
      //   k6 run --out influxdb=http://localhost:8086/jaltantra_metrics soak_test.js
    },
  },

  thresholds: {
    // keep 99% of requests under 800 ms
    http_req_duration: ['p(99)<800'],
    // allow no more than 0.5% failures
    checks: ['rate>0.995'],
    // ensure no VU drops below 50 while holding
    vus: ['value>=50'],
  },
};

const BASE = 'http://localhost:8090/jaltantra_loop_dev_v7';

export default function () {
  // — Public pages
  group('Public Pages', () => {
    let res = http.get(`${BASE}/`);
    check(res, {
      'home is status 200': (r) => r.status === 200,
    });

    res = http.get(`${BASE}/login`);
    check(res, {
      'login page is 200': (r) => r.status === 200,
    });
  });

  // — Authenticate
  group('Login', () => {
    const payload = {
      username: __ENV.USERNAME,
      password: __ENV.PASSWORD,
    };
    const params = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      redirects: 0,
    };
    let loginRes = http.post(`${BASE}/login`, payload, params);
    check(loginRes, {
      'login status is 302': (r) => r.status === 302,
      'session cookie set': (r) =>
        !!(r.cookies['JSESSIONID'] || r.cookies['SESSION']),
    });
  });

  // — Authenticated pages
  group('Authenticated Pages', () => {
    let res = http.get(`${BASE}/loop`);
    check(res, { 'loop page is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/branch`);
    check(res, { 'branch page is 200': (r) => r.status === 200 });
  });

  // simulate realistic think time
  sleep(1 + Math.random() * 2);
}

