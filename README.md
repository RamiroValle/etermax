# Playlists API REST

Application made in Java and Spring Boot

## Requirements

1. JDK 17+

## How to run

1. Fork the project
2. ./gradlew bootRun
3. The server is now running on localhost:8080

First authenticate via user/pass to get a Token:
```bash
curl -X POST user:password@localhost:8080/login
```
This should return a bearer Token for use in future requests.
```bash
export TOKEN={token}
curl -X POST localhost:8080/api/playlists -H "Authorization: Bearer $TOKEN" -D {playlistJson}
```
Then you will be able to test the different endpoints.

### Run tests
```bash
./gradlew test
```
