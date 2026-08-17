# Plan: implement `POST /api/returns`

See `issues/02-deposit-return.md` for the full ticket.

## Open questions

### `quantity: 0`

**Decision:** reject with `400` — a return line needs at least one item.

Reasoning: a line with zero items hands nothing back and pays out nothing, so
it can only be a client mistake; rejecting it up front is clearer than
silently emitting a useless `depositCents: 0` line.

### negative quantity

**Decision:** reject with `400` — same rule as above, one `@Positive`
constraint on `Item.quantity` covers both.

Reasoning: a negative quantity has no physical counterpart (you cannot hand
back minus three bottles), and letting it through would multiply into a
negative `depositCents`, which `DepositCalculator`'s characterized behaviour
shows happens today with no guard.

`ReturnRequest.Item.quantity` carries no validation today and is never
actually exercised — the endpoint 501s unconditionally regardless of the
body. So adding `@Positive` is new behaviour, not a change to an existing,
tested code path, and needs no characterization test of its own first.

### upper limit per return

**Decision:** none — don't add one.

Reasoning: neither the ticket's acceptance criteria nor any other part of the
codebase calls for a cap; inventing one now would be speculative validation
for a scenario nobody has asked to handle.

## Validation precedence

**Decision:** an invalid `quantity` always wins over an unknown `productId`
in the same request.

Reasoning: `@Valid` runs before the controller body executes, so if a request
has one item with `quantity: 0` and a different item with an unknown
`productId`, Bean Validation rejects the whole request with `400` before the
repository lookup for either item ever runs. This isn't a design choice made
in `ReturnController` — it's a consequence of using declarative validation —
but it needs a test so the behaviour is pinned down rather than accidental.

## Already checked, no action needed

- **Error body shape.** `ApiErrorController` already turns any dispatched
  error, including Bean Validation failures, into an RFC 9457 body
  generically (see `ApiErrorControllerTest`). The new `@Positive` violation
  reuses that path — no new error-handling code required.
- **`ApiDocsTest`.** It asserts on path existence, path count, and path
  names, never on response schema. `/api/returns` is already
  `@PostMapping`-mapped with `ReturnReceipt` as its declared return type, so
  springdoc already reflects that path and shape today, stub or not.
  Implementing the method body changes neither. No update needed.

## Steps

1. Add characterization tests to `DepositCalculatorTest` covering every
   `PackagingType` (`REUSABLE_GLASS`, `REUSABLE_PLASTIC`, `NO_DEPOSIT`, plus
   `rateInCents` directly for all five) and unusual quantities (`0`,
   negative) — no production code touched. Run them against the unmodified
   class and confirm they're green. Commit on its own.
2. Add a `@Positive` constraint to `ReturnRequest.Item.quantity`, matching
   the existing `@NotEmpty`/`@NotBlank` style on the same record (see
   reasoning above for why this needs no characterization step first).
3. Implement `ReturnController.calculateReturn`: for each request item, look
   up the product via `ProductRepository.findById`, throw
   `ResponseStatusException(NOT_FOUND)` for an empty `Optional`, compute
   `depositPerItemCents` via `DepositCalculator.rateInCents` and
   `depositCents` via `depositInCents`, and collect one `ReturnReceipt.Line`
   per item in request order, summing `totalDepositCents`. Remove the class
   Javadoc's "the logic does not [exist] — see README.md" sentence, since it
   stops being true.
4. Replace `ReturnControllerTest`'s 501 test and its now-stale class Javadoc
   ("the endpoint is reachable but has no logic...") with tests for the
   implemented behaviour:
   - happy path with multiple lines in request order, asserting
     `productName`, `depositPerItemCents`, `depositCents`, and
     `totalDepositCents` individually
   - a `NO_DEPOSIT` product appearing at 0 cents
   - unknown `productId` returning 404
   - `quantity` 0 and negative `quantity` each returning 400
   - a request with one item failing `@Positive` and another item carrying
     an unknown `productId`, returning 400 (pins the validation-precedence
     decision above)

   Keep the existing empty-list 400 test.
5. Update `docs/api.md`'s `POST /api/returns` section: drop the "not
   implemented" note, describe the actual behaviour including the 404 and
   400 cases, and document the new "quantity must be positive" constraint on
   request items.
6. Run `./mvnw verify` and confirm it's green.
7. Commit the implementation (steps 2–4) separately from the docs update
   (step 5), keeping the characterization-test commit from step 1 first and
   untouched.
