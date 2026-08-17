# AGENTS.md

Exercise project for the REWE AI Enablement Base AI Training: a small Spring Boot
deposit-return service on Java 21.

Work items are tracked as tickets in `issues/`. When you implement one, read it in
full first — acceptance criteria and open questions live there, not in this file.

## Commands

```bash
./mvnw spring-boot:run                     # run the service on http://localhost:8080
./mvnw test                                # tests only
./mvnw verify                              # format check + tests + jar
./mvnw spotless:apply                      # format the code
./mvnw test -Dtest=ClassName#methodName    # a single test
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

`./mvnw test` deliberately skips the format check so the test loop stays fast.
`./mvnw verify` is what has to be green before calling anything done.

Running it in a container instead — no local JDK needed:

```bash
docker compose up --build          # start it (or: docker build -t rewe-training .)
docker compose logs -f             # follow the log
docker compose exec app sh         # shell inside the running container
docker compose down                # stop it
```

Port 8080 is mapped, so the service is reached exactly as it is when started
locally — `curl http://localhost:8080/api/products`, Swagger UI at `/`. Nothing
about calling the API changes.

Two limits worth knowing before you try:

- **Tests cannot run in the container.** The runtime image is a JRE holding only
  the jar — no Maven, no sources. `./mvnw test` runs on the host.
- **The image build skips the tests**, and a code change needs a rebuild — there
  is no hot reload. A green `docker compose up` therefore says nothing about
  correctness. `./mvnw verify` remains the gate.

## Architecture

- **Four packages.** `catalog`, `deposit` and `returns` are cut by topic, `error`
  is cross-cutting. There is no `service` package: behaviour lives in domain
  classes such as `DepositCalculator`, and controllers call repositories
  directly. Don't add a service layer for logic of this size.
- **A new class goes into the package of its topic.** If it fits none of them,
  that means a topic is missing — not that a technical package is needed. No
  `util`, no `common`, no `helper`.
- **Import direction**, per the current graph: `catalog` and `error` import from
  no other package, `deposit` imports from `catalog`, `returns` from both. Never
  add an import running the other way.
- **`ProductRepository` stays in-memory.** This project is about the workflow,
  not persistence — don't introduce a database or external store for it.
- **`PackagingType` holds no behaviour.** Deposit rates live in
  `DepositCalculator.RATES_IN_CENTS`, keyed by the enum. Don't move per-type
  logic into the enum itself.
- The rates exist twice: in `DepositCalculator` and, for humans, in
  `docs/api.md`. `DepositCalculator` is the source of truth — keep `docs/api.md`
  in sync with it.
- The API documentation is generated from the controllers by springdoc. Don't
  hand-maintain anything that duplicates it; the paths are configured in
  `application.yaml`.

## Conventions

- **Money is always `int` cents**, never `double` or `BigDecimal`. Any name
  carrying an amount ends in `Cents` — `priceCents`, `depositCents`,
  `depositPerItemCents`, `rateInCents`.
- **Records for data, classes for behaviour.** Use records for DTOs and entities,
  including nested records for sub-objects (`ReturnRequest.Item`,
  `ReturnReceipt.Line`). Don't reintroduce classes with getters and setters for
  these. Document every record component with a Javadoc `@param` line, as
  `Product` does.
- **Constructor injection**, never field `@Autowired`, no Lombok. Both existing
  controllers follow this.
- **Repositories return `Optional`** — never null, never a thrown exception. The
  caller decides what "not found" means (`ProductRepository.findById`).
- **Validation is declarative**, via `jakarta.validation` annotations on the
  record components themselves (`@NotEmpty`, `@NotBlank`), cascaded into nested
  records with `@Valid`. See `ReturnRequest`.
- **Not-found lookups throw `ResponseStatusException`** directly in the
  controller, as `ProductController` does. `@RestControllerAdvice` is reserved for
  translating exception types thrown deeper in the code, as `NotImplementedAdvice`
  does for `UnsupportedOperationException`.
- **Every error response is an RFC 9457 `ProblemDetail` JSON body**, including
  unmapped paths — never Spring's Whitelabel HTML page. `ApiErrorController`'s
  `/error` mapping is `@Hidden` from the OpenAPI docs, because `/error` is a
  servlet mechanism and not part of the API.

## Testing

- Controller tests: `@SpringBootTest` + `@AutoConfigureMockMvc` + `MockMvc`. Pure
  logic tests such as `DepositCalculatorTest`: plain JUnit and AssertJ, no Spring
  context.
- Build fixtures with small private static factory methods, not mocks — see
  `DepositCalculatorTest.product(...)`.
- Name test methods `methodUnderTest_condition_expectedResult`, each with a
  `@DisplayName` sentence.
- `ApiDocsTest` pins the shape of the generated OpenAPI document — the number of
  paths, `/error` being absent, `/` redirecting to the Swagger UI. Update it
  whenever an endpoint is added or removed.
- **Before changing existing behaviour that lacks test coverage**, first add
  characterization tests that pin down what the code does today, in their own
  commit, separate from any behaviour change. Never change structure and
  behaviour at the same time. A test that faithfully records wrong behaviour is
  still a correct characterization test. This is how this project works with
  existing code, not a step tied to one class or one ticket.

## Repo hygiene

- Never commit `maven-wrapper.jar`. `mvnw` and `mvnw.cmd` fetch Maven themselves
  on first use.
- Keep the Docker build multi-stage — build tools never ship in the runtime layer
  — and keep it running as a non-root user.
