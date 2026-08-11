# rewe-training

The exercise project for the AI Enablement Base AI Training.

It is deliberately small: a product catalogue that works, and one endpoint that
does not. Building that endpoint is the exercise.

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

## The exercise

`POST /api/returns` takes the items a customer hands back at the deposit return
machine and should answer with a receipt: one line per product and the total
deposit to be paid out.

Everything you need is already there: `ProductRepository` resolves an article
number, `DepositCalculator` knows the rates. `ReturnController` has both
injected and throws `UnsupportedOperationException`.

The acceptance criteria and the edge cases are in the issue tracker.

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
