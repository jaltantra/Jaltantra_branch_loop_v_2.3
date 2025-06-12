import http from 'k6/http';
import { check, group, sleep } from 'k6';

//––– CONFIGURATION –––
export let options = {
  vus: 1,            // number of virtual users
  duration: '30s',   // total test duration
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% of requests must finish <500ms
  },
};

// Base URL of your app
const BASE = 'https://www.cse.iitb.ac.in/jaltantra_loop_dev_v7';

export default function () {
  // Grouping makes the console output easier to read
  group('🎯 Smoke Test: Public Pages', () => {
    let res;

    // 1. Home
    res = http.get(`${BASE}/`);
    check(res, {
      'home: status is 200': (r) => r.status === 200,
      'home: body is non-empty': (r) => r.body.length > 100,
    });

    // 2. Login page
    res = http.get(`${BASE}/login`);
    check(res, { 'login page: status is 200': (r) => r.status === 200 });

    // 3. Register page
    res = http.get(`${BASE}/register`);
    check(res, { 'register page: status is 200': (r) => r.status === 200 });

    // 4. Loop endpoint (GET form)
    res = http.get(`${BASE}/loop`);
    check(res, { 'loop: status is 200': (r) => r.status === 200 });

    // 5. Branch endpoint (GET form)
    res = http.get(`${BASE}/branch`);
    check(res, { 'branch: status is 200': (r) => r.status === 200 });
  });

  // Pause between iterations
  sleep(1);
}

