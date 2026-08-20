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

🚧 Early stage — built as a learning project in Java/Spring Boot. The
domain model is in place; the service layer and REST API are not built
yet.

## Domain model

- **`RoleModel`** — a job function or specialization (e.g. "Backend
  Developer"), with a name and description.
- **`ResourceModel`** — a resource that can do work: a set of roles, an
  hourly price, and a type (`HUMAN` or `LLM`, nested inside `ResourceModel`
  since it's never meaningful on its own).
- **`TaskModel`** — a single piece of work: a description, the resource
  assigned to it, an estimated execution time and an estimated
  verification time. Its `price()` is calculated on demand
  (`hourlyPrice × total time`), not stored, so it can never drift out of
  sync with the underlying numbers.
- **`ProjectModel`** — a customer engagement: a customer name, a project
  name, and a growing list of `Task`s (added via `addTask(...)`). Its
  `totalPrice()` is likewise calculated on demand by summing every task's
  price.

`RoleModel` and `ResourceModel` are records — accurately immutable
snapshots. `ProjectModel` is a regular mutable class, since its task list
is meant to grow over the course of a planning session.

## How to run

**Prerequisites:** a JDK compatible with Spring Boot 4.1.0 (Java 17–26;
this project targets Java 25). No separate Maven install is needed — the
project ships with the Maven Wrapper.

From the project root:

```bash
# macOS/Linux
./mvnw spring-boot:run

# Windows (PowerShell)
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`. There are no REST
endpoints exposed yet — see "Planned functionality" below.

### Building a jar

```bash
./mvnw clean package
java -jar target/costcompass-0.0.1-SNAPSHOT.jar
```

## Implemented

- Domain model: `RoleModel`, `ResourceModel`, `TaskModel`, `ProjectModel`
- Calculated pricing per task and per project (no stored/derived-data
  drift)

## Planned functionality

- Service layer for storing and retrieving resources, tasks, and projects
  (in-memory to start, same pattern as TestPulse)
- REST API for creating/listing resources and building up a project's
  task list
- An AI-assisted layer to help reason about a proposed price for a given
  set of tasks and resources (later phase — ties into the AI track)
- Persistent storage (database, replacing the in-memory store)
- Automated tests (JUnit 5 / Mockito)

## Tech stack

- Java / Spring Boot
- Maven
- JUnit 5 / Mockito (planned)
