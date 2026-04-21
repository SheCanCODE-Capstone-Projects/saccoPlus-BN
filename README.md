# SACCOPlus — Backend (Spring Boot)

## Structure
```
src/main/java/com/saccoplus/
├── SaccoPlusApplication.java   # Entry point
├── config/                     # SecurityConfig, CorsConfig
├── controller/                 # REST controllers (one per domain)
├── dto/
│   ├── request/                # Incoming request POJOs + validation
│   └── response/               # Outgoing response POJOs
├── entity/                     # JPA entities
├── exception/                  # GlobalExceptionHandler, custom exceptions
├── repository/                 # Spring Data JPA interfaces
├── security/                   # JwtFilter, JwtUtils, UserDetailsServiceImpl
├── service/                    # Business logic interfaces
│   └── impl/                   # Implementations
└── util/                       # Helpers (calculators, schedulers)

src/main/resources/
├── application.yml             # Dev + prod profiles
└── templates/email/            # Email templates
```

## Profiles
| Profile | DDL     | SQL logging | Use |
|---------|---------|-------------|-----|
| `dev`   | update  | enabled     | Local dev |
| `prod`  | validate| disabled    | Production |

## Commands
```bash
mvn spring-boot:run                              # run with dev profile
mvn test                                         # run all tests
mvn package -DskipTests                          # build JAR
mvn spring-boot:run -Dspring-boot.run.profiles=prod  # run prod profile
```

## Adding an endpoint
1. Add entity in `entity/`
2. Add repository in `repository/`
3. Add DTOs in `dto/request/` and `dto/response/`
4. Add service interface in `service/`
5. Add implementation in `service/impl/`
6. Add controller in `controller/`
7. Add tests in `src/test/`
