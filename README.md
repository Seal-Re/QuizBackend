# QuizBackend · Spring Boot Quiz Question Bank API

> **A minimal Spring Boot REST API serving quiz questions from MongoDB — clean layered architecture with repository pattern.**
>
> 基于 Spring Boot + MongoDB 的题库 REST API，仓储模式分层，为 QuizFrontend 提供题目数据服务。

[English](#english) · [中文](#中文)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.5-6DB33F?logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-5.x-47A248?logo=mongodb)
![Java](https://img.shields.io/badge/Java-11+-ED8B00?logo=openjdk)
![License](https://img.shields.io/badge/License-MIT-blue)

---

<a id="english"></a>

## Architecture

```mermaid
flowchart LR
    Frontend["QuizFrontend\n(Vue 3)"] -->|GET /api/questions| ApiController
    subgraph QuizBackend["QuizBackend (Spring Boot)"]
        ApiController --> ApiService
        ApiService --> ApiServiceImpl
        ApiServiceImpl --> QuestionRepository
        QuestionRepository --> MongoDB[("MongoDB\nquestions collection")]
    end
```

## Quickstart

```bash
# 1. Start MongoDB (local or Atlas)
# 2. Configure application.properties
mvn spring-boot:run
```

### Config (`src/main/resources/application.properties`)

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/vidar
spring.data.mongodb.database=vidar
```

## API

| Method | Path | Response |
|---|---|---|
| `GET` | `/api/questions` | `QuestionsDto` — all questions with options + answer |

## Domain Model

```
Questions (MongoDB document)
├── question  String   question text
├── options   List     answer choices
└── answer    String   correct answer
```

## Technical Highlights

<details>
<summary><b>Repository pattern with Spring Data MongoDB</b></summary>

- **S**: MongoDB document queries need to be decoupled from business logic to stay testable and swappable.
- **A**: `QuestionRepository` extends `MongoRepository<Questions, String>`. `ApiServiceImpl` calls the repository; the controller only handles HTTP concerns. BO/DTO separation (`Questions` BO → `QuestionsDto`) prevents leaking the storage model to the API contract.
- **R**: Swapping the MongoDB collection schema only requires changing the BO; DTO and controller unchanged.
</details>

## Repo Layout

```
src/main/java/com/seal/vidar/
├── controller/ApiController.java      GET /api/questions
├── service/
│   ├── ApiService.java               interface
│   └── impl/ApiServiceImpl.java      MongoDB query logic
├── entity/
│   ├── bo/Questions.java             MongoDB document model
│   └── dto/QuestionsDto.java         API response shape
└── repository/QuestionRepository.java MongoRepository
```

## Roadmap

- [x] `GET /api/questions` — full question bank
- [ ] Question pagination and category filter
- [ ] `POST /api/questions` — admin question upload
- [ ] Score submission and leaderboard endpoints

---

<a id="中文"></a>

## 中文速读

- **是什么**：QuizFrontend 的后端题库 API，Spring Boot + MongoDB，`GET /api/questions` 返回全量题目（题干、选项、答案）。
- **亮点**：BO/DTO 分离隔离存储层与 API 契约，`MongoRepository` 仓储模式使数据层可替换。
- **运行**：配置 `application.properties` 中 MongoDB URI → `mvn spring-boot:run`。

## License

MIT © [Seal-Re](https://github.com/Seal-Re)
