# SPRING BOOT BLOG REST API

## Technologies

### Tech Stack

- Java 21
- Spring Boot 3.2
- H2 Database
- Maven

### Dependency

- Spring Boot Starter Web
- Spring Boot Starter JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Dev Tools
- Lombok
- H2 Database
- Modal Mapper 3.1.1
- Spring Doc Open API UI 1.7.0
- JJWT-API 0.12.3
- JJWT-Impl 0.12.3
- JJWT-Jackson 0.12.3

## API SPEC

### Endpoints:

#### AUTH

- POST /api/v1/auth/sign-up

Request Body:

```json
{
  "name": "Steve",
  "email": "steve@example.com",
  "password": "password",
  "role": "admin" // admin or writer
}
```

- POST /api/v1/auth/sign-in

Request Body:

```json
{
  "email": "steve@example.com",
  "password": "password"
}
```

#### USER

Only user with "Admin" role can access all user's endpoints.

- GET /api/v1/users?page=0&size=1&sort=id,desc
- GET /api/v1/users?name=name
- GET /api/v1/users/1
- POST /api/v1/users

Request Body:

```json
{
  "name": "Steve",
  "email": "steve@example.com",
  "password": "password",
  "role": "admin" // admin or writer
}
```

- PUT /api/v1/users

Request Body:

```json
{
  "id": 1,
  "name": "Steven",
  "email": "steven@example.com",
  "password": "password",
  "role": "writer" // admin or writer
}
```

- DELETE /api/v1/users/1

#### POST

Only user with "Writer" role can access all post's endpoints.

- GET /api/v1/posts?page=0&size=1&sort=id,desc
- GET /api/v1/posts?title=title
- GET /api/v1/posts/1
- POST /api/v1/posts

Request Body:

```json
{
  "title": "Title 1",
  "body": "Body 1",
  "author": "Author 1"
}
```

- PUT /api/v1/posts

Request Body:

```json
{
  "id": 1,
  "title": "Title 1 updated",
  "body": "Body 1 updated",
  "author": "Author 1 updated"
}
```

- DELETE /api/v1/posts/1
