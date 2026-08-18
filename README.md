# rewe-training

The exercise project for the AI Enablement Base AI Training.

It is deliberately small: a product catalogue that works, and one endpoint that
does not. Building that endpoint is the exercise.

**Doing the training?** Start with [docs/playbook.md](docs/playbook.md) — every
block with its steps and prompts.

## What you need

| Tool              | Version                            | Needed for                          |
|-------------------|------------------------------------|-------------------------------------|
| JDK               | 21                                 | building and running the service    |
| IntelliJ IDEA     | 2023.3 or newer, Community is fine | working on the code                 |
| Claude Code CLI   | current                            | every exercise in the training      |
| Git               | any                                | cloning this repository             |
| Docker            | 24 or newer — optional             | running the service without a JDK   |

Maven is not on the list: `mvnw` downloads it.

Install steps and a check per tool: [docs/setup.md](docs/setup.md).

## Run

Three ways, same result. Pick one.

### Terminal

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

### IntelliJ IDEA

1. **File | Open**, pick this folder. IDEA imports the Maven project on its own.
2. **File | Project Structure | Project** — set the SDK to 21.
3. Open `TrainingApplication`, start it with the green arrow next to `main`.

Tests: right-click `src/test/java`, then **Run 'All Tests'**.

### Docker

No JDK needed, the image brings its own:

```bash
docker compose up --build
```

Without compose:

```bash
docker build -t rewe-training .
docker run --rm -p 8080:8080 rewe-training
```

## Check that it runs

Open `http://localhost:8080`. The API documentation appears, and every endpoint
can be called from there with **Try it out** — no curl, no Postman.

```bash
curl http://localhost:8080/api/products
```

## Endpoints

| Method | Path                 | Status                    |
|--------|----------------------|---------------------------|
| GET    | `/api/products`      | works                     |
| GET    | `/api/products/{id}` | works                     |
| POST   | `/api/returns`       | **not implemented — 501** |

Request and response formats, deposit rates and the error format:
[docs/api.md](docs/api.md).

## The exercises

The tickets live in [issues/](issues/), so you can work from the repository
alone:

| File | What it asks for |
|------|------------------|
| [01-products-filterable.md](issues/01-products-filterable.md) | narrow the product list down |
| [02-deposit-return.md](issues/02-deposit-return.md) | make `POST /api/returns` work |

The second one is the main exercise. Everything it needs is already there:
`ProductRepository` resolves an article number, `DepositCalculator` knows the
rates. `ReturnController` has both injected and throws
`UnsupportedOperationException`.

## Build

```bash
./mvnw test            # run the tests
./mvnw verify          # format check, tests, jar
./mvnw spotless:apply  # format the code
```

## Layout

```
de/rewe/training/
  TrainingApplication   entry point
  catalog/              what the store sells
  deposit/              what a package is worth
  returns/              what a customer hands back
  error/                every failure answers as JSON
```

The tests mirror that structure.
