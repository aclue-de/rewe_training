# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repository is

Exercise project for the REWE "Base AI Training". A small Spring Boot 3.3 / Java 21
service. Endpoints are built as exercises from the tickets in `issues/` — an
unfinished endpoint throws `UnsupportedOperationException` on purpose. Never
implement, complete or "fix" one unless the current task asks for it.

`docs/playbook.md` is the training script; the tickets in `issues/` are the tasks.

## Commands

```bash
./mvnw test                      # tests only — no format check, fast loop
./mvnw verify                    # spotless:check + tests + jar (the gate)
./mvnw spotless:apply            # format Java and sort pom.xml
./mvnw spring-boot:run           # http://localhost:8080 → Swagger UI

./mvnw test -Dtest=DepositCalculatorTest                       # one test class
./mvnw test -Dtest=DepositCalculatorTest#depositInCents_crate_returns150
```

Use `-B -ntp` for non-interactive runs. Never `-q`: it hides the `Tests run:` and
`BUILD SUCCESS` lines — read both before reporting a build green. Windows uses
`mvnw.cmd` instead of `./mvnw`.

Validation-failure tests log `WARN` lines from Spring's binding — expected output,
not a failure.

Docker (optional; the image build skips tests, so it is no substitute for `verify`):

```bash
docker compose up --build        # run the service
docker compose run --rm verify   # full Maven build in a container, no local JDK needed
```

## Architecture

Packages under `de.rewe.training` are cut **by topic, not by layer** — there is no
service layer and this project does not want one. Controllers talk to
`@Component`/`@Repository` collaborators directly.

| Package   | Holds                                                                        |
|-----------|------------------------------------------------------------------------------|
| `catalog` | `Product` record, `PackagingType` enum, in-memory `ProductRepository`, `ProductController` |
| `deposit` | `DepositCalculator` — the per-packaging deposit rate table                    |
| `returns` | `ReturnController`, `ReturnRequest` / `ReturnReceipt` records                  |
| `error`   | every failure answers as JSON, never HTML                                    |

Dependency direction: `returns` → `catalog` and `deposit`. Never the reverse.
`catalog` and `deposit` do not know about `returns`.

Key decisions worth knowing before editing:

- **All money is `int` cents.** No `double`, no `BigDecimal`, field names end in
  `Cents` (`Product.priceCents`, `ReturnReceipt.totalDepositCents`).
- **Collaborators are `private final` fields injected through a single constructor**
  — no `@Autowired`, no setters, no Lombok (`ProductController.java:15`). The fields
  are named for the role they play, not after their type: `products`, `calculator`
  (`ReturnController.java:20`).
- **Lookups return `Optional`, never `null`**, and never decide the HTTP meaning
  themselves — the controller does (`ProductRepository.java:30`).
- **`ProductRepository` is an immutable in-memory seed list** of 8 products
  (`ProductRepository.java:16`). Tests assert against those exact ids, names and
  counts, so changing the seed breaks tests on purpose.
- **Errors are RFC 9457 `ProblemDetail`, everywhere.** `ApiErrorController` replaces
  the Whitelabel page for the servlet `/error` dispatch; `NotImplementedAdvice` maps
  `UnsupportedOperationException` → 501. Controllers signal HTTP failure by throwing
  `ResponseStatusException` (see `ProductController.findById`), not by returning
  `ResponseEntity` with a status.
- **Validation is declarative** on the request records (`@NotEmpty`, `@NotBlank`,
  `@Valid` on nested items) plus `@Valid` on the `@RequestBody` — no manual checks
  in controllers.
- **The base path lives in a class-level `@RequestMapping`**; method annotations
  carry only the remaining segment, or nothing (`ProductController.java:12`).
- **OpenAPI is generated from the code**, and `ApiDocsTest` asserts that exactly
  three paths are documented. A new endpoint or a leaked internal path fails that
  test; internal endpoints get `@Hidden`.
- Domain types are `record`s with Javadoc `@param` lines documenting units and format.

## Conventions

- **Formatting is enforced**: Spotless with palantir-java-format, import order,
  unused-import removal. Bound to `verify`, deliberately not to `test`. Run
  `./mvnw spotless:apply` before committing. `.editorconfig`: 4 spaces for Java,
  2 for yaml/xml/json, LF (CRLF only for `.cmd`/`.bat`).
- **Test naming**: `method_scenario_expectation` (`findById_unknownId_returns404`)
  plus an `@DisplayName` written as a full sentence.
- Test classes and test methods are package-private — no `public`
  (`ReturnControllerTest.java:22`).
- Controller/error tests are `@SpringBootTest` + `@AutoConfigureMockMvc` with
  `MockMvc` and `jsonPath`; pure logic (`DepositCalculatorTest`) is a plain JUnit
  class with AssertJ and no Spring context. Tests mirror the main package layout.
- JSON request bodies in tests are inline text blocks, not fixture files and not
  built through an `ObjectMapper` (`ReturnControllerTest.java:30`).
- Comments and Javadoc explain *why* something is the way it is (see the class
  Javadoc on `ProductRepository` and `ApiErrorController`) — match that tone rather
  than restating the code.
- There is no `maven-wrapper.jar` in the repo and there must never be one — `mvnw`
  downloads Maven on first use.
