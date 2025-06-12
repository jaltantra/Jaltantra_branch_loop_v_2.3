import http from 'k6/http';
import { check, group, sleep } from 'k6';

export let options = {
  vus: 1,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<500'],
  },
};

const BASE = 'https://www.cse.iitb.ac.in/jaltantra_loop_dev_v7';

export default function () {
  // 1. Public smoke tests
  group('🎯 Smoke Test: Public Pages', () => {
    let res = http.get(`${BASE}/`);
    check(res, {
      'home: 200': (r) => r.status === 200,
    });

    res = http.get(`${BASE}/login`);
    check(res, { 'login page: 200': (r) => r.status === 200 });
  });

  // 2. Login
  group('🔐 Authenticate', () => {
    const payload = {
      username: __ENV.USERNAME,
      password: __ENV.PASSWORD,
    };
    // the default k6 cookie jar will capture Set-Cookie headers
    const loginRes = http.post(`${BASE}/login`, payload, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      redirects: 0, // don't follow the 302 automatically
    });
    check(loginRes, {
      'login status is 302': (r) => r.status === 302,
      'received session cookie': (r) => r.cookies['JSESSIONID'] || r.cookies['SESSION'],
    });
  });

  // 3. Authenticated smoke tests
  group('🔄 Smoke Test: Authenticated Pages', () => {
    let res = http.get(`${BASE}/loop`);    // now sends the saved cookie
    check(res, { 'loop (authed): 200': (r) => r.status === 200 });

    res = http.get(`${BASE}/branch`);
    check(res, { 'branch (authed): 200': (r) => r.status === 200 });
  });

  sleep(1);
}



