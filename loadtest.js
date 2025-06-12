import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 50 }, // ramp-up to 50 users
    { duration: '3m', target: 50 }, // stay at 50 users
    { duration: '1m', target: 0 },  // ramp-down
  ],
  thresholds: {
    // 95% of requests must finish below 800ms
    http_req_duration: ['p(95)<800'],
    // less than 1% of checks may fail
    checks: ['rate>0.99'],
  },
};

const BASE = 'https://www.cse.iitb.ac.in/jaltantra_loop_dev_v7';

export default function () {
  // PUBLIC ENDPOINTS
  group('Public Pages', () => {
    let res = http.get(`${BASE}/`);
    check(res, { 'home is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/login`);
    check(res, { 'login page is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/register`);
    check(res, { 'register page is 200': (r) => r.status === 200 });
  });

  // AUTHENTICATION
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
      'has session cookie': (r) =>
        !!(r.cookies['JSESSIONID'] || r.cookies['SESSION']),
    });
  });

  // AUTHENTICATED ENDPOINTS
  group('Authenticated Pages', () => {
    let res = http.get(`${BASE}/loop`);
    check(res, { 'loop is 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/branch`);
    check(res, { 'branch is 200': (r) => r.status === 200 });

    // if you have any API endpoints, hit them here:
    // res = http.get(`${BASE}/api/status`);
    // check(res, { 'api status 200': (r) => r.status === 200 });
  });

  // Simulate user think-time
  sleep(Math.random() * 3 + 1);
}


