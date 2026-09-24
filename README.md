# CostCompass

A planning tool for pricing consulting engagements that mix AI agents and
human resources.

## The problem

As AI agents take on more of the actual implementation work, the old
"hours × hourly rate" pricing model breaks down — an agent might produce
in minutes what used to take a human days, but a human still has to review,
verify, and test the result. Figuring out what to actually charge a
customer, and which mix of resources makes sense for a given engagement,
becomes harder to reason about without a tool.

CostCompass lets you model the resources available for a project (human
roles and AI agents, each with their own hourly rate), attach them to
concrete tasks with an estimated execution time and verification time, and
get back a calculated price — both per task and for the whole customer
proposal.

## Status

🚧 Under active development, built as a learning project in Java/Spring
Boot and React/TypeScript. The domain model, service layer, and a full
REST API (list/create/update/delete) are in place for all four resources
(Roles, Resources, Tasks, Projects), backed by an in-memory store. Domain
changes are published as Kafka events. A React frontend covers all four
list views plus a Dashboard and an AI Chat screen; a couple of pieces on
both sides are still mid-build (see "Planned functionality").

## Domain model

- **`RoleModel`** — a job function or specialization (e.g. "Backend
  Developer"), with a name and description.
- **`ResourceModel`** — a resource that can do work: a set of roles, an
  hourly price, and a type (`HUMAN` or `AI_DIRECTED`, nested inside
  `ResourceModel` since it's never meaningful on its own).
- **`TaskModel`** — a single piece of work: a description, the resource
  assigned to it, an estimated execution time and an estimated
  verification time. Its `price()` is calculated on demand
  (`hourlyPrice × total time`), not stored, so it can never drift out of
  sync with the underlying numbers.
- **`ProjectModel`** — a customer engagement: a customer name, a project
  name, and a growing list of `TaskModel`s (added via `addTask(...)`). Its
  `price()` is likewise calculated on demand by summing every task's
  price.

`RoleModel`, `ResourceModel` and `TaskModel` are records — accurately
immutable snapshots. `ProjectModel` is a regular mutable class, since its
task list is meant to grow over the course of a planning session.

Every create/update/delete on a Role or Task is published as a domain
event (`RoleEvent`, `TaskEvent`, ...) to a dedicated Kafka topic
(`role-events`, `task-events`, ...); the equivalent event types exist for
Resources and Projects but aren't wired into their services yet.

## How to run

**Prerequisites:** a JDK compatible with Spring Boot 4.1.0 (Java 17–26;
this project targets Java 25), Docker, and Node.js. No separate Maven
install is needed — the project ships with the Maven Wrapper.

1. **Start Kafka** (from the project root)

   ```bash
   docker compose up -d
   ```

2. **Run the backend**

   ```bash
   # macOS/Linux
   ./mvnw spring-boot:run

   # Windows (PowerShell)
   .\mvnw.cmd spring-boot:run
   ```

   The API is served at `http://localhost:8080/api/costcompass`. Kafka
   connection settings are in `src/main/resources/application.properties`.

   **AI Chat (OpenAI):** the AI Chat feature calls OpenAI, so it needs an
   API key. Create `src/main/resources/application-local.properties`
   (add it to `.gitignore` — it holds a real secret and must never be
   committed) with:

   ```properties
   spring.ai.openai.api-key=your-api-key-here
   ```

   then start the backend with the `local` profile active so that file
   gets picked up:

   ```bash
   # macOS/Linux
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

   # Windows (PowerShell) — quote the whole -D argument; otherwise
   # PowerShell can split it into two tokens and Maven fails with
   # "Unknown lifecycle phase"
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
   ```

3. **Run the frontend** (from `frontend/`)

   ```bash
   npm install
   npm run dev
   ```

   Served at `http://localhost:5173` by default; CORS for that origin is
   already configured on the backend (`WebConfig`).

Java source changes need a rebuild + restart to take effect — there's no
hot reload configured (e.g. Spring DevTools) yet.

### Building a jar

```bash
./mvnw clean package
java -jar target/costcompass-0.0.1-SNAPSHOT.jar
```

## Implemented

- Domain model: `RoleModel`, `ResourceModel`, `TaskModel`, `ProjectModel`
- Calculated pricing per task and per project (no stored/derived-data
  drift)
- REST API (list/create/update/delete) for Roles, Resources, Tasks and
  Projects
- Kafka domain events for Roles and Tasks (create/update/delete)
- React + TypeScript frontend: list views for all four resources, a
  Dashboard with live KPIs, and an AI Chat screen (UI only so far)

## Planned functionality

- Kafka events for Resources and Projects (event types exist, not yet
  published from their services)
- AI-assisted layer to help reason about a proposed price for a given set
  of tasks and resources — a first Spring AI + Ollama endpoint exists as a
  spike (`scratch/AiTestController`), not yet wired to the AI Chat screen
- Persistent storage (database, replacing the in-memory store)
- Automated tests (JUnit 5 / Mockito)

## Tech stack

**Backend:** Java 25, Spring Boot 4, Spring for Apache Kafka, Spring AI
(Ollama), Maven, JUnit 5 / Mockito (planned)

**Frontend:** React, TypeScript, Tailwind CSS, Vite

**Infrastructure:** Apache Kafka (Docker Compose, single-broker KRaft
setup — no separate ZooKeeper needed)
