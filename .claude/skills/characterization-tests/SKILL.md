---
name: characterization-tests
description: Add characterization tests that pin down what a class currently does, before its behaviour changes. Use when a class lacks test coverage and is about to be modified.
---

# Characterization tests for a class

A characterization test pins down what a class does **today**, not what it
should do. It exists to make an upcoming change safe, not to specify correct
behavior. Never combine a characterization-test commit with a structure or
behavior change — the whole point is that the tests are provably true of the
code before anything moves.

## When to use this

- A ticket asks you to change or extend a class whose current tests don't
  cover every branch you're about to touch.
- You're about to refactor a class with weak coverage and want a safety net
  first.
- Not needed when the class already has adequate tests for the paths you're
  about to change — extend those instead of restating them as
  "characterization."

## Process

1. **Read the class in full**, including every branch, default, and fallback
   — not just the happy path. Trace what it actually does for boundary
   inputs the signature allows (`0`, negative numbers, empty collections,
   unknown ids/keys, an enum's less-obvious values) by reading the
   implementation, never by guessing intended behavior.
2. **List the tests that already exist** for the class and note which
   branches they cover. Characterization tests fill the gaps — don't restate
   a test that's already there and still accurate.
3. **Write one test per branch or edge case**, asserting exactly what the
   code does now — including when that looks wrong or surprising. Per
   `AGENTS.md`: "a test that captures wrong behaviour is still a correct
   characterization test." Don't editorialize about what it *should* do in
   the assertion.
4. **Run `./mvnw verify` and confirm it is green** — not just the new tests. A
   characterization test that fails on today's code is a bug in the test, not a
   finding: fix the assertion, not the class. `verify` also runs the format
   check, so the commit lands formatted.
5. **Commit the characterization tests on their own**, before touching the
   class. Message states what it does, e.g. `Add characterization tests for
   DepositCalculator`.
6. Only after that commit lands, make the intended change — updating or
   replacing the specific tests whose expected behavior legitimately shifts.
   Leave tests for branches you didn't touch as they are.

## Which test style

Match this repo's existing split by kind of class:

- **Plain domain/logic class** (e.g. `DepositCalculator`): plain JUnit 5 +
  AssertJ, no Spring context. Build inputs with small private static factory
  methods, not mocks — see `DepositCalculatorTest.product(...)`.
- **Controller**: `@SpringBootTest` + `@AutoConfigureMockMvc` + `MockMvc`,
  asserting on status code and JSON body via `jsonPath`.

If the class has no test file yet, create one in the mirrored test package,
named `<ClassName>Test`.

## Naming and structure

- Method names: `methodUnderTest_condition_expectedResult`.
- Every test gets a `@DisplayName` sentence describing the observed
  behavior, phrased as what happens, not what should happen — e.g. `"a
  negative quantity returns a negative deposit"`.
- If most or all tests in the file characterize a known placeholder or gap
  (a stub endpoint, an unfinished branch), add a short class-level Javadoc
  saying so and that the tests go away once real behavior lands — see
  `ReturnControllerTest` for the pattern. Skip this comment for ordinary
  characterization tests where there's no gap to explain; the default in
  this repo is no comments unless the "why" is non-obvious.

## What to cover

Enumerate branches by reading the source, not by imagining a spec:

- every distinct case a `switch`/`Map`/`if` chain can take, including the
  `default`/fallback,
- boundary and unusual inputs the signature allows: `0`, negative numbers,
  empty collections, unknown ids,
- anything already special-cased (e.g. an enum value mapped to a `0` rate).

Don't invent cases the code can't reach, and don't assert on behavior you
haven't verified by reading the implementation.

## Worked example in this repo

`issues/02-deposit-return.md` asks for exactly this: before implementing the
deposit-return endpoint, pin down `DepositCalculator.rateInCents` and
`depositInCents` for every `PackagingType`, plus `0` and negative
quantities — in a commit of its own, ahead of any endpoint work.
