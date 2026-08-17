# Implement the deposit return endpoint

`POST /api/returns` currently answers with 501. It should return a receipt for
the items a customer hands back.

## Request

```json
{ "items": [ { "productId": "P-1001", "quantity": 6 } ] }
```

## Response

```json
{
  "lines": [
    {
      "productId": "P-1001",
      "productName": "Sparkling water 0.5 l",
      "quantity": 6,
      "depositPerItemCents": 25,
      "depositCents": 150
    }
  ],
  "totalDepositCents": 150
}
```

## Before you change anything

`DepositCalculator` holds the rates this endpoint depends on, and its test
coverage has gaps. Before you touch anything, pin down what it does today — for
**every** packaging type, and for unusual quantities such as zero or a negative
number. Put those tests in a separate commit.

Characterization tests record what the code does today, not what it should do.
A test that captures wrong behaviour is still a correct characterization test.
Never change structure and behaviour at the same time.

## Acceptance criteria

- one line per item in the request, in the order they were sent
- `depositCents` is `depositPerItemCents * quantity`
- `totalDepositCents` is the sum of all lines
- an unknown `productId` results in `404`
- products with `NO_DEPOSIT` appear on the receipt with 0 cents
- `./mvnw verify` is green

## Affected code

- `returns/ReturnController` — the endpoint, today the stub that throws
- `returns/ReturnRequest`, `returns/ReturnReceipt` — request and response
  records, both already in place
- `catalog/ProductRepository.findById` — resolves the article number and returns
  `Optional`. The controller decides what an empty `Optional` means.
- `deposit/DepositCalculator` — `rateInCents` for the rate, `depositInCents` for
  rate times quantity

## Architecture

- `returns` may depend on `catalog` and `deposit`, never the other way round
- this project has no service layer and this change does not introduce one:
  packages are cut by topic, not by layer
- both collaborators are already constructor-injected into `ReturnController`

## Open questions — decide and document

- what happens with `quantity: 0`?
- what happens with a negative quantity?
- is there an upper limit per return?
