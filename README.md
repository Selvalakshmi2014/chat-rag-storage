**RAG Chat Storage Microservice**
**Overview**

This backend microservice stores and manages chat histories for a Retrieval-Augmented Generation (RAG) AI chatbot system. 
It provides:

* User chat sessions with messages
* 
* Session management (rename, favorite, delete)
* 
* Pagination for messages
* 
* Secure API access using API key authentication
* 
* Rate limiting to prevent abuse
* 
* Dockerized setup for local development
* 
* Tech Stack: Java 21, Spring Boot, PostgreSQL, Docker, Maven

**Features**

* Session Management: Create, rename, mark favorite, delete sessions
* 
* Message Storage: Store messages with sender type (USER/AI), content, and optional context
* 
* Pagination: Retrieve messages using page and size parameters
* 
* Security: API key authentication and rate limiting
* 
* Logging: Centralized logging with configurable log file rotation
* 
* Health Check: /actuator/health endpoint
* 
* Dockerized: App, PostgreSQL, and pgAdmin for easy setup

**Setup & Docker**
1. Clone the Repository
   git clone <repo_url>
   cd <repo_folder>
2. Configure Environment
   cp .env.example .env

Edit .env to set environment variables:
DB_URL, DB_USERNAME, DB_PASSWORD, DB_NAME – Database connection
API_KEY – API key for secure requests
Logging and rate-limit settings as needed

3. Start Services with Docker:   
   docker-compose up --build -d

| Service        | Description            | URL                                            |
| -------------- | ---------------------- | ---------------------------------------------- |
| **App**        | Chat backend API       | [http://localhost:8080](http://localhost:8080) |
| **PostgreSQL** | Database               | db:5432                                        |
| **pgAdmin**    | Database management UI | [http://localhost:5050](http://localhost:5050) |

**pgAdmin Login:**

Email: admin@local.com

Password: admin

Host: db, Port: 5432

The docker-compose.yml in the project root defines all services. No additional setup is required.

**API Endpoints**
**Sessions**
| Method | Endpoint                                   | Description           |
| ------ | ----------------------------------------- | -------------------- |
| GET    | `/api/v1/sessions`                        | Get all sessions     |
| POST   | `/api/v1/sessions`                        | Create a new session |
| PATCH  | `/api/v1/sessions/{id}/rename`            | Rename a session     |
| PATCH  | `/api/v1/sessions/{id}/favorite?favorite=true|false` | Mark/unmark favorite |
| DELETE | `/api/v1/sessions/{id}`                   | Delete a session     |
**Messages**
| Method | Endpoint                                       | Description                 |
| ------ | ---------------------------------------------- | --------------------------- |
| GET    | `/api/v1/messages/{session_id}?page=0&size=10` | Retrieve paginated messages |
| POST   | `/api/v1/messages`                             | Add message to a session    |

Note: All API requests require header: X-API-KEY: <your-api-key>
**Health Check:** 
GET /actuator/health
Returns the health status of the service and database connectivity.

**Swagger Doc:**
GET /swagger-ui/index.html

**Testing:**
Unit tests are implemented for services:
 mvn clean test

**Architecture:**  
Client → API → Service → Repository → PostgreSQL   

Controller: Handles HTTP requests
Service: Contains business logic
Repository: Handles database operations

**Feature:**
1. API KEY authentication
2. Rate limiting
3. Global Error Handling and logging
4. Dockerized application
5. Health checks (/actuator/health)
6. Swagger/OpenAPI documentation
7. Dockerized pgAdmin for database management
8. Unit tests for services
9. CORS configuration for frontend integration
10. Pagination support

**Author**

Selvalakshmi – Backend Developer
Submission for Backend Developer Interview Case Study: RAG Chat Storage Microservice