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
list views plus a Dashboard and an AI Chat screen wired to OpenAI. A
separate internal Dev Assistant (chat, autonomous agent, and a task board)
helps the team track development work on CostCompass itself, backed by
Postgres. A couple of pieces on both sides are still mid-build (see
"Planned functionality").

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

This domain model is still backed by an in-memory store — see "Planned
functionality".

## Dev Assistant (internal tooling)

Separate from the customer-facing AI Chat, the team has an internal
development assistant for tracking work on CostCompass itself, backed by
its own Postgres database:

- **Dev Chat** (`/dev-chat` in the frontend, `POST
  /api/costcompass/dev/assistant/request`) — remembers decisions, bugs and
  notes across sessions, and manages structured development tasks (title,
  status, type, assignee). Can also read the whole codebase (backend and
  frontend) on request to suggest improvements, or to verify whether an
  existing task is actually done instead of trusting the stored status
  blindly.
- **Autonomous agent** — runs on a schedule, scans unresolved bug/task
  memories, searches the codebase for the relevant file(s), creates
  tracked tasks automatically, and marks the source memory resolved so it
  never repeats itself. Can be triggered manually for testing via `POST
  /api/costcompass/dev/agent/run`.
- **Dev Tasks board** (`/dev-tasks` in the frontend) — a read-only,
  Jira-style board showing all tasks grouped by status.

## How to run

**Prerequisites:** a JDK compatible with Spring Boot 4.1.0 (Java 17–26;
this project targets Java 25), Docker, and Node.js. No separate Maven
install is needed — the project ships with the Maven Wrapper.

1. **Start Kafka and Postgres** (from the project root)

   Postgres needs a password before its first start. Create a `.env` file
   next to `docker-compose.yml` (add it to `.gitignore` — never commit it):

   ```
   POSTGRES_PASSWORD=your-local-password
   ```

   Then:

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

   **Local secrets and dev-tooling paths:** the AI Chat feature calls
   OpenAI, and the Dev Assistant needs a database connection and the
   absolute paths to your source folders. Create
   `src/main/resources/application-local.properties` (add it to
   `.gitignore` — it holds real secrets and must never be committed) with:

   ```properties
   spring.ai.openai.api-key=your-api-key-here
   spring.datasource.password=your-local-password
   dev.source-path=/absolute/path/to/src/main/java
   dev.frontend-source-path=/absolute/path/to/frontend/src
   ```

   `spring.datasource.password` must match the `POSTGRES_PASSWORD` you set
   in `.env`.

   Then start the backend with the `local` profile active so that file
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
  Dashboard with live KPIs, and an AI Chat screen wired to OpenAI via
  Spring AI
- Internal Dev Assistant: chat with persistent memory, structured
  development tasks, an autonomous background agent, and a task board
  (Postgres-backed)

## Planned functionality

- Kafka events for Resources and Projects (event types exist, not yet
  published from their services)
- Persistent storage for the core domain (database, replacing the
  in-memory store for Roles/Resources/Tasks/Projects) — only the internal
  Dev Assistant is Postgres-backed so far
- Interactive Dev Tasks board (drag-and-drop status changes; currently
  read-only)
- Automated tests (JUnit 5 / Mockito)

## Tech stack

**Backend:** Java 25, Spring Boot 4, Spring for Apache Kafka, Spring AI
(OpenAI), Maven, JUnit 5 / Mockito (planned)

**Frontend:** React, TypeScript, Tailwind CSS, Vite

**Infrastructure:** Apache Kafka (Docker Compose, single-broker KRaft
setup — no separate ZooKeeper needed), PostgreSQL (Docker Compose)
