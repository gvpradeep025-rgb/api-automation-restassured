# API Test Cases: Restful Booker

Base URL: https://restful-booker.herokuapp.com · Auth: POST /auth with admin / password123 returns a token, sent as `Cookie: token=<token>`

| ID | Endpoint | Scenario | Expected status | Key checks | Type | Automated |
|----|----------|----------|-----------------|-----------|------|-----------|
| API_01 | GET /ping | Health check | 201 | - | Smoke | Yes |
| API_02 | POST /auth | Valid credentials | 200 | Token is a non-empty string | Positive | Yes |
| API_03 | POST /auth | Wrong password | 200 | reason = "Bad credentials", no token | Negative | Yes |
| API_04 | POST /booking | Create booking | 200 | bookingid > 0, fields match request, JSON schema valid, < 5 s | Positive | Yes |
| API_05 | GET /booking/{id} | Get created booking | 200 | Fields match, JSON schema valid | Positive | Yes |
| API_06 | PUT /booking/{id} | Full update with token | 200 | All fields replaced | Positive | Yes |
| API_07 | PATCH /booking/{id} | Partial update with token | 200 | Only sent fields change | Positive | Yes |
| API_08 | DELETE /booking/{id} | Delete with token | 201 | - | Positive | Yes |
| API_09 | GET /booking/{id} | Get deleted booking | 404 | - | Negative | Yes |
| API_10 | GET /booking/{id} | Non-existing id | 404 | - | Negative | Yes |
| API_11 | PUT /booking/{id} | No token | 403 | - | Security | Yes |
| API_12 | DELETE /booking/{id} | Invalid token | 403 | - | Security | Yes |
| API_13 | POST /booking | Missing required fields | 500 (should be 400) | Logged as a bug, see below | Negative | Yes |
| API_14 | GET /booking | List all | 200 | Non-empty list of ids | Positive | Yes |
| API_15 | GET /booking?firstname=&lastname= | Filter by name | 200 | Created id is in the result | Positive | Yes |
| API_16 | POST /booking | Data-driven: 3 bookings from JSON | 200 | Each field saved correctly | Positive | Yes |

## Bugs found

### BUG-API-001: Missing fields return 500 instead of 400
- **Endpoint:** POST /booking
- **Steps:** Send `{ "firstname": "OnlyName" }`
- **Expected:** 400 Bad Request with a message saying which fields are missing
- **Actual:** 500 Internal Server Error
- **Severity:** Medium · **Priority:** Medium

### BUG-API-002: Wrong credentials return 200 OK
- **Endpoint:** POST /auth
- **Steps:** Send a wrong password
- **Expected:** 401 Unauthorized
- **Actual:** 200 OK with `{ "reason": "Bad credentials" }`
- **Severity:** Low · **Priority:** Medium

### BUG-API-003: Delete returns 201 Created
- **Endpoint:** DELETE /booking/{id}
- **Expected:** 200 OK or 204 No Content
- **Actual:** 201 Created, which is the wrong status code for a delete
- **Severity:** Low · **Priority:** Low
