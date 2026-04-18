# Smart Campus API

A RESTful API for managing university Smart Campus rooms, sensors, and sensor readings.
Built with JAX-RS (Jersey) and Grizzly embedded server.

---

## Tech Stack
- Java 17
- JAX-RS / Jersey 3.1.3
- Grizzly HTTP Server
- Jackson (JSON)
- Maven

---

## How to Build and Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps
```bash
git clone https://github.com/YOUR_USERNAME/smart-campus-api.git
cd smart-campus-api
mvn clean package
java -jar target/smart-campus-api-1.0-SNAPSHOT.jar
```
API will be running at: http://localhost:8080/api/v1/

---

## Endpoints Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/ | Discovery / HATEOAS |
| GET | /api/v1/rooms | Get all rooms |
| POST | /api/v1/rooms | Create a room |
| GET | /api/v1/rooms/{id} | Get room by ID |
| DELETE | /api/v1/rooms/{id} | Delete a room |
| POST | /api/v1/sensors | Create a sensor |
| GET | /api/v1/sensors | Get all sensors |
| GET | /api/v1/sensors?type={type} | Filter sensors by type |
| GET | /api/v1/sensors/{id} | Get sensor by ID |
| GET | /api/v1/sensors/{id}/readings | Get all readings |
| POST | /api/v1/sensors/{id}/readings | Add a reading |

---

## curl Examples

```bash
# 1. Discovery
curl -X GET http://localhost:8080/api/v1/

# 2. Get all rooms
curl -X GET http://localhost:8080/api/v1/rooms

# 3. Create a sensor
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"type":"Oxygen","status":"ACTIVE","currentValue":95.0,"roomId":"SCU_YOGA-03"}'

# 4. Post a reading
curl -X POST http://localhost:8080/api/v1/sensors/{id}/readings \
  -H "Content-Type: application/json" \
  -d '{"value":42.5}'

# 5. Trigger 409 error
curl -X DELETE http://localhost:8080/api/v1/rooms/SCU_YOGA-03
```

---

## Report Questions

### Q1) JAX-RS Resource Lifecycle
By default, JAX-RS creates a new instance of a resource class (e.g. RoomResource) for every incoming request. This means the resource class itself cannot hold any data it gets thrown away after each request. To solve this, we use a separate DataStore class with static fields, which exists independently of any request and persists for the lifetime of the application. Since multiple requests can arrive simultaneously and access the same DataStore, we use ConcurrentHashMap instead of a regular HashMap to prevent race conditions and data corruption.

### Q2) HATEOAS
Standard APIs provide you with data and nothing more. HATEOAS indicates that your API provides the client with navigation by incorporating links in the response. Similar to our /api/v1/, it provides links to /rooms and /sensors. The client doesn't have to speculate or consult documentation the API provides guidance. Imagine it as a website featuring clickable links compared to one that merely displays text without any navigation

### Q3) IDs vs Full Objects in List Response
When you provide a list, you have two options ;
1) Provide complete objects (all fields) convenient for the client, but burdensome if there are 1000 rooms. 
2) Provide only IDs, minimal response, but the client needs to make additional requests for details.
Our API provides complete objects. That works well for small datasets but may be slow when scaled up. The tradeoff is convenience versus bandwidth

### Q4) Is DELETE Idempotent?
Idempotent means calling the same request multiple times produces the same result. In our API, the first DELETE on a room returns 204 No Content, and a second DELETE on the same room returns 404. While the status code differs, REST still considers DELETE idempotent because the outcome is the same both times,the room no longer exists. The 404 simply reflects reality, not a failure.

### Q5) Content-Type Mismatch
JAX-RS examines the Content-Type header prior to executing your method. If it does not align with what your method @Consumes, JAX-RS will automatically deny it with a 415 Unsupported Media Type error. Your code doesn't run at all.

### Q6) @QueryParam vs Path Segment
Path segments such as /sensors/type/CO2 indicate that CO2 is a distinct resource possessing its own identity. However, filtering is not a resource it serves as an optional search on a collection. @QueryParam such as ?type=CO2 clarifies that this represents optional filtering. It is also possible to easily merge several filters, such as ?type=CO2&status=ACTIVE

### Q7) Sub-Resource Locator Pattern
Rather than consolidating all endpoints into a single large class, the locator pattern enables you to divide responsibilities. SensorResource manages sensors, while SensorReadingResource manages readings. Every class is more compact, clearer to understand, and simpler to manage. It naturally reflects the URL structure readings fall under sensors.

### Q8) 422 vs 404
404 signifies "I wasn't able to locate the URL you sought." However, the URL /sensors is completely functional. The issue lies within the request body the roomId you submitted is non-existent. The JSON was correct, the endpoint is present, but the content was semantically incorrect. 422 signifies "I received your request, but I was unable to process it due to the data being nonsensical."

### Q9) Stack Trace Security Risk
Stack traces expose your internal code architecture class identifiers, method identifiers, line numbers, library versions. A malicious user can exploit this to discover existing weaknesses in your libraries, comprehend your code logic, and devise specific attacks. It's essentially giving a plan of your system to unknown individuals. Our GenericExceptionMapper avoids this by providing a generic message instead

### Q10) Filters vs Logger calls
Placing Logger.info() in every resource method results in redundant code repetition numerous times. Should you ever wish to modify the log format, you must revise each individual method. A filter operates automatically on each request and response without modifying your resource classes. This is referred to as a cross-cutting concern  a single place manages it for the entire application