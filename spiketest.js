import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 10 },    // baseline
    { duration: '30s', target: 300 },  // spike up
    { duration: '1m', target: 300 },   // hold spike
    { duration: '30s', target: 10 },   // spike down
    { duration: '1m', target: 10 },    // recovery
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'], // 95% of requests <1s
    checks: ['rate>0.99'],             // 99% of checks must pass
  },
};

const BASE = 'https://www.cse.iitb.ac.in/jaltantra_loop_dev_v7';

export default function () {
  // 1) Public endpoints
  group('Public Endpoints', () => {
    let res = http.get(`${BASE}/`);
    check(res, { 'home is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/login`);
    check(res, { 'login page is 200': (r) => r.status === 200 });
  });

  // 2) Login
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

  // 3) Authenticated endpoints
  group('Authenticated Endpoints', () => {
    let res = http.get(`${BASE}/loop`);
    check(res, { 'loop is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/branch`);
    check(res, { 'branch is 200': (r) => r.status === 200 });
  });

  // simulate user think-time
  sleep(Math.random() * 2 + 1);
}

