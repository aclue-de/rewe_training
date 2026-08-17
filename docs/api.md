# API

Base URL `http://localhost:8080`. The documentation at `/` is generated from the
controllers, so it cannot go stale — and **Try it out** calls every endpoint
straight from the browser.

The OpenAPI document itself is at `/v3/api-docs`.

## GET /api/products

The whole assortment.

```bash
curl http://localhost:8080/api/products
```

```json
[
  {
    "id": "P-1001",
    "name": "Sparkling water 0.5 l",
    "priceCents": 79,
    "packaging": "SINGLE_USE"
  }
]
```

## GET /api/products/{id}

One product by article number. Unknown id answers `404`.

```bash
curl http://localhost:8080/api/products/P-1003
```

## POST /api/returns

One line per item in the request, in the order they were sent, plus the
total to pay out. A product with `NO_DEPOSIT` packaging appears on the
receipt with `0` cents.

`quantity` must be `1` or more — `0`, a negative number, or an empty `items`
list all answer `400`. An unknown `productId` answers `404`.

The request holds what the customer handed back:

```json
{
  "items": [
    { "productId": "P-1001", "quantity": 6 },
    { "productId": "P-1004", "quantity": 1 }
  ]
}
```

It answers with a receipt:

```json
{
  "lines": [
    {
      "productId": "P-1001",
      "productName": "Sparkling water 0.5 l",
      "quantity": 6,
      "depositPerItemCents": 25,
      "depositCents": 150
    },
    {
      "productId": "P-1004",
      "productName": "Lager crate 20 x 0.5 l",
      "quantity": 1,
      "depositPerItemCents": 150,
      "depositCents": 150
    }
  ],
  "totalDepositCents": 300
}
```

## Deposit rates

| Packaging          | Cents |
|--------------------|-------|
| `SINGLE_USE`       | 25    |
| `REUSABLE_GLASS`   | 8     |
| `REUSABLE_PLASTIC` | 15    |
| `CRATE`            | 150   |
| `NO_DEPOSIT`       | 0     |

## Errors

Every error answers as a problem detail (RFC 9457), never as HTML — a typo in
the path included:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Unknown path. The API documentation is at /",
  "path": "/typo"
}
```
