# API Automation Framework: REST Assured + Postman

![API Tests](https://github.com/gvpradeep025-rgb/api-automation-restassured/actions/workflows/api-tests.yml/badge.svg)

Automated API testing for the [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html) hotel booking API, done two ways:

1. **REST Assured + Java + TestNG** framework, with POJOs, JSON schema validation, data-driven tests and HTML reports.
2. **Postman collection** with test scripts, run automatically by **Newman** in CI.

Both run on every push and every morning on **GitHub Actions**.

## What it covers

| Area | Tests |
|------|-------|
| Health | API is up |
| Authentication | Valid token · wrong password rejected |
| Booking CRUD (chained) | Create → Read → Update (PUT) → Partial update (PATCH) → Delete → Verify 404 |
| Negative & security | Non-existing id · no token · invalid token · missing fields |
| Search | List all · filter by name |
| Data-driven | 3 bookings from a JSON file |
| Contract | JSON schema validation on create and get |
| Performance | Response time under 5 seconds |

18 REST Assured test runs + 8 Postman requests with 15 assertions. 3 real API bugs found and documented in [docs/api-test-cases.md](docs/api-test-cases.md).

## Tech stack

Java 11+ · REST Assured 5 · TestNG · Jackson · JSON Schema Validator · ExtentReports · Postman · Newman · Maven · GitHub Actions

## Project structure

```
src/test/java/com/pradeep/api
├── base/BaseTest.java              # shared request & response specifications
├── clients/                        # one method per endpoint (like Page Objects for APIs)
│   ├── AuthClient.java
│   └── BookingClient.java
├── models/                         # POJOs for serialization / deserialization
│   ├── Booking.java
│   ├── BookingDates.java
│   └── BookingResponse.java
├── utils/
│   ├── ConfigManager.java          # reads config.properties, overridable with -D
│   └── TestDataFactory.java        # random data + JSON test data reader
├── listeners/ExtentReportListener.java
└── tests/
    ├── HealthCheckTest.java
    ├── AuthTest.java
    ├── BookingCrudTest.java        # end-to-end chained flow
    ├── BookingDataDrivenTest.java
    ├── BookingNegativeTest.java
    └── BookingFilterTest.java

src/test/resources
├── config.properties
├── schemas/                        # JSON schemas for contract testing
└── testdata/bookings.json

postman/restful-booker.postman_collection.json
docs/api-test-cases.md              # 16 test cases + 3 bug reports
.github/workflows/api-tests.yml     # CI: REST Assured job + Newman job
```

## How to run

```bash
# REST Assured tests (needs Java 11+ and Maven)
mvn clean test
# Report: reports/api-test-report.html

# Postman collection (needs Node.js)
npm install -g newman
newman run postman/restful-booker.postman_collection.json
```

Or import the collection into Postman and click **Run collection**.

## Key concepts used

- **Request and response specifications** so headers, base URI and response-time checks are written once.
- **API client layer**: tests call `bookings.createBooking(booking)` instead of raw HTTP code, so an endpoint change is fixed in one place.
- **POJO serialization / deserialization** with Jackson.
- **Request chaining**: the booking id from the create call is reused in get, update and delete; the auth token is passed as a cookie.
- **JSON schema validation** to catch contract changes (a missing field or wrong type).
- **Data-driven testing** from a JSON file with TestNG `@DataProvider`.
- **Test dependencies** (`dependsOnMethods`) so later CRUD steps skip cleanly if create fails.
- **Positive, negative and security tests**, including status-code bugs found in the API.
- **ExtentReports** HTML report and **Newman htmlextra** report uploaded as CI artifacts.
