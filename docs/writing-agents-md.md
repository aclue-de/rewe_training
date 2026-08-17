# Writing an instruction file

`/init` gets you a draft in a minute. Turning that draft into a file worth keeping
is the actual work, and it is the same work in every repository. The examples below
come from this one.

## The file itself

Put the rules in `AGENTS.md`. Leave a `CLAUDE.md` next to it containing one line:

```markdown
@AGENTS.md
```

`@` is an import, not prose: the content is pulled into context as if it were
written there. A markdown link would only be a sentence the model might act on.
One home for the content, found under either name.

## Nine things to get right

### 1. Write rules, not observations

`/init` describes what it sees. A description expires; a rule does not.

> ❌ `ReturnController` currently throws `UnsupportedOperationException`.
> ✅ An endpoint that is not implemented yet throws `UnsupportedOperationException`
> and `NotImplementedAdvice` maps it to a 501 — don't return a placeholder
> response instead.

Same subject, and the difference is what happens when the code moves on. The first
sentence becomes false the moment that endpoint is implemented, and nobody will
notice — leaving a statement in the file that the agent still reads as
authoritative. The second is just as true afterwards, and it applies to the next
stub as well.

The test: *does this sentence tell me what to do next time, or only what happens
to be the case right now?* "Currently throws" answers nothing about a change you
are about to make. If a line has no rule behind it at all, delete it rather than
rephrasing it.

### 2. Apply the expiry test to every line

*Would this still be true in six months, once the current work is done?* If not,
it belongs in a ticket or a commit message.

> ❌ Building the real implementation is exercise 02.

### 3. Ground every rule in the codebase, not in a ticket

A rule whose only authority is a closed ticket has no authority at all.

> ❌ Don't add a service layer — issue 02 says not to introduce one.
> ✅ There is no `service` package: behaviour lives in domain classes such as
> `DepositCalculator`, and controllers call repositories directly.

Same rule, real reason. The second version survives the ticket being closed — and
it tells you *why*, which is what lets someone apply it to a case the rule didn't
foresee.

### 4. Never let task detail become policy

This is the expensive mistake. A one-time instruction from a ticket, lifted into
the instruction file, becomes a law that applies everywhere — and it drags the
ticket's specifics along with it.

> ❌ Before changing `DepositCalculator`, test every `PackagingType` and the
> quantities zero and negative.
> ✅ Before changing existing behaviour that lacks test coverage, first pin down
> what the code does today, in its own commit.

The practice is worth keeping. The class name and the list of gaps are not — they
belong to one piece of work, and repeating them here hands out an answer that the
next person should find for themselves.

### 5. Keep only the duplication that cannot silently go wrong

Not all repetition is bad. Before deleting, ask: *would a wrong copy cause damage,
and would anyone notice?*

- **Keep** the build commands. Yes, they follow from the build config — and that
  is the point: without them, every session starts by reading it.
- **Keep** the reasoning: *why* `test` skips the format check and `verify` does
  not. That reasoning exists nowhere else in usable form.
- **Drop** configured values. This file used to name the Swagger UI path. It is
  set in `application.yaml`; changing it there touches no Java file, so the
  instruction file would just start lying.

### 6. Write down what is written nowhere else

The rules that only live in people's heads are the ones worth the file. `/init`
cannot find them, because nothing in the repository states them.

In this project the sharpest example is money: every amount is an `int` in cents
and every name carrying one ends in `Cents`. It is visible in `priceCents`,
`depositCents` and `rateInCents` — and stated in no README, no comment, no config.
An agent that hasn't been told will reach for `BigDecimal` on the next money field,
and be entirely reasonable about it.

### 7. Demand evidence for every rule

Ask for a `file:line` per claim. Without it you get plausible inventions —
conventions nobody agreed on, phrased with total confidence, permanent once
written.

Two real examples from this file's own history: a prohibition on "a second
formatter alongside Spotless", which no one had ever decided, and a claim that
`verify` is "the CI gate" in a repository that has no CI at all. Both were written
by the same agent that later found them, when asked where each rule came from.

### 8. Say what not to do, where the wrong move is tempting

A rule that only states the happy path won't stop a plausible mistake.

> ✅ `PackagingType` holds no behaviour — the rates live in `DepositCalculator`.
> Don't move per-type logic into the enum itself.

Putting the rate on the enum is a perfectly sensible-looking idea. That is exactly
why the prohibition earns its line.

### 9. Treat length as a cost

Every line is read in every session. A rule that has never changed anyone's
behaviour is noise competing with the rules that matter. When in doubt, cut it —
and split by topic with `@` imports before letting one file grow past skimming
length.

## Where each fact belongs

| Fact | Home |
|------|------|
| Rules that hold for every change | `AGENTS.md` |
| What this one change must do | the ticket |
| Why a past decision was made | commit message, or an ADR |
| Configured values, generated docs | the config, the generator |
| Your personal workflow | your own settings, not the repo |

## Checklist

- [ ] Every line still true in six months
- [ ] Every rule grounded in the code, not in a ticket
- [ ] No ticket specifics — no class names or edge cases from one task
- [ ] Duplication only where a wrong copy would be noticed
- [ ] The unwritten conventions written down
- [ ] Every rule traceable to a place in the code
- [ ] Prohibitions where the wrong move is tempting
- [ ] Nothing that has never changed a decision
