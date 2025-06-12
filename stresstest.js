import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  // a stress profile: stepwise ramp-up to extreme user counts
  stages: [
    { duration: '2m', target: 100 },  // ramp to 100 VUs over 2m
    { duration: '3m', target: 100 },  // hold 100 VUs for 3m
    { duration: '2m', target: 200 },  // ramp to 200 VUs over 2m
    { duration: '3m', target: 200 },  // hold 200 VUs for 3m
    { duration: '2m', target: 400 },  // ramp to 400 VUs over 2m
    { duration: '3m', target: 400 },  // hold 400 VUs for 3m
    { duration: '2m', target: 0 },    // ramp-down to 0
  ],
  thresholds: {
    // allow some failures but watch response times spike
    http_req_duration: ['p(95)<1200'],   // 95% under 1200ms
    'checks': ['rate>0.98'],             // at least 95% of checks must pass
  },
};

const BASE = 'https://www.cse.iitb.ac.in/jaltantra_loop_dev_v7';

export default function () {
  // 1) Public endpoints
  group('Public Endpoints', () => {
    let res = http.get(`${BASE}/`);
    check(res, { 'home status 200': (r) => r.status === 200 });
    res = http.get(`${BASE}/login`);
    check(res, { 'login page 200': (r) => r.status === 200 });
  });

  // 2) Authentication
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
      'login status 302': (r) => r.status === 302,
      'session cookie set': (r) =>
        !!(r.cookies['JSESSIONID'] || r.cookies['SESSION']),
    });
  });

  // 3) Authenticated operations
  group('Authenticated Endpoints', () => {
    let res = http.get(`${BASE}/loop`);
    check(res, { 'loop 200': (r) => r.status === 200 });
    res = http.get(`${BASE}/branch`);
    check(res, { 'branch 200': (r) => r.status === 200 });
  });

  // 4) Think time
  sleep(Math.random() * 2 + 1);
}

