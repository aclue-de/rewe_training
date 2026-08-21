# Playbook

What to do in each hands-on block: the goal, the prompts to copy, the commands,
and how you know you are finished. Two blocks are live demos — you watch those.

**Getting through the list is not the point.** The point is a way of working where
you stay in control of what the agent does — where you can say afterwards which
decisions were yours and which it made for you. If a block runs out of time on
that question, it did its job.

| Block | | Result |
|-------|-----|--------|
| **H1** First session | 35 min | `AGENTS.md` |
| **H2** Right-sizing | 30 min | a filled comparison grid |
| **H3** Your own skill | 35 min | `SKILL.md` |
| **D1** Subagent | 8 min | live demo, nothing to build |
| **D2** Team configuration | 8 min | live demo, nothing to build |
| **H6** The big block | 45 min | the endpoint, reviewed |

The two tickets for the day are in [../issues](../issues). Setup instructions are
in [setup.md](setup.md), the API is described in [api.md](api.md).

## Before you start

Teams of two or three. One branch per team, off `main`, and stay on it for the
whole day.

```bash
git switch -c training main
./mvnw -B -ntp clean verify        # no local JDK: docker compose run --rm verify
```

Expected: `Tests run: 13, Failures: 0` and `BUILD SUCCESS`. Don't use `-q`, it
hides both lines. The `WARN` about `must not be empty` during the tests is not an
error — it is the test for the 400 response.

Working from the container build? Then every `./mvnw` line below is
`docker compose run --rm verify` for you, as the comments note. Put that command in
your `AGENTS.md` in H1, otherwise Claude Code keeps reaching for `./mvnw`.

Commit after every block. At the end, the whole day is one branch of history.

---

## H1 · First session — 35 min

**Goal:** an `AGENTS.md` that describes this project.

### 1 · Let `/init` run

```
/init
```

It writes a `CLAUDE.md`. Don't read it and nod — the next two prompts work on it.

### 2 · Remove what does not belong

```
Audit this instruction file. For each rule ask two things: does it exist because
of how this codebase is built, or because of one ticket or task? And would it
still be true once the current work is finished?

At most five findings, one line each, worst first, no prose. Then stop — do not
edit anything.
```

### 3 · Add what is missing

```
Ignore this file and read the code instead. Name up to five conventions this
codebase follows that the file does not mention. One line each, with one
file:line where the convention is visible. No prose.
```

The `file:line` matters. Without a place to check, you get conventions that sound
plausible and were never agreed on.

Never have the whole file rewritten in one pass: let it propose, decide yourself,
then apply.

### 4 · Rename and commit

The file is called `AGENTS.md`. `CLAUDE.md` keeps one line that imports it.

```bash
mv CLAUDE.md AGENTS.md
echo "@AGENTS.md" > CLAUDE.md
git add -A && git commit -m "Add AGENTS.md"
```

`@AGENTS.md` is an import, not a reference: the content lands in the context as if
it were written there.

**Done when:**

- `AGENTS.md` exists and `CLAUDE.md` imports it
- it holds conventions that `/init` did not find on its own
- no line in it would be false once today's work is finished

---

## H2 · Right-sizing — 30 min

**Goal:** the same task twice — once handed over as it stands, once properly
briefed — and the two results compared. Ticket:
[01-products-filterable.md](../issues/01-products-filterable.md).

You are not here to understand the service. Nobody understands an unfamiliar
codebase in ten minutes, and you don't need to: what you are comparing is the two
prompts, not the two implementations.

No branch switching. Run 1 gets discarded, run 2 stays. Fill each column while
that run is still there.

| | Run 1 | Run 2 |
|---|---|---|
| Files / lines | | |
| Decisions made without asking | | |
| Written down anywhere? | | |
| `verify` green? | | |

Rows 2 and 3 are not filled by reading the diff — in unfamiliar code nobody sees
what was never asked. Ask instead, after each run:

```
Which decisions did you make that the ticket did not specify? For each, say where
it is written down. At most five lines, one per decision, no explanation.
```

Expect a long answer anyway — the last sentence helps, it does not guarantee. That
is fine: write down **how many** decisions it names and move on. Copy one or two
out only if they interest you. Reading the whole answer is not what the block is
for.

### Run 1 — 10 min

```
Implement what issues/01-products-filterable.md asks for. Just get it done.
```

Two of the four rows come from here, the other two from the question above:

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
git --no-pager diff --stat
```

A red `verify` here is a finding, not a reason to stop.

Once the grid is filled, wipe both the context and the tree — otherwise run 2
inherits what run 1 wrote and just repeats it:

```
/clear
```

```bash
git reset --hard && git clean -fd
```

### Run 2 — 10 min

The counter-question first, then the brief. The order matters.

```
Before I brief you: what have I overlooked in issues/01-products-filterable.md?
Which decisions does the ticket leave open?
```

Then the brief, in four blocks. The angle brackets are yours to fill:

```
Context: issues/01-products-filterable.md, GET /api/products in catalog/.
The conventions are in AGENTS.md.

Goal: <the expected behaviour, not the solution>

Constraints:
- without the parameter, the response is exactly as it is today
- <one line per open question the counter-question raised, with your decision>
- no refactoring on the side, no new package
- the build is green

Format: a diff plus tests — including one that checks the error path returns a
ProblemDetail body, not just the right status.
```

Conventions do not belong in the brief, they are in `AGENTS.md`. Leave one of the
four blocks out and the agent guesses at exactly that point.

Fill the right column the same way — these two rows plus the question — then keep
it:

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
git add -A && git commit -m "Filter the product list by packaging type"
```

### Compare — 10 min

The grid is filled. Put the list of open questions from the counter-question next
to it and go through them one by one.

A brief does not remove decisions, it moves them down. Run 1 guesses at the
contract: parameter name, one route or two, one value or many. Your brief settles
those — and that lets the work get concrete enough for a new layer to appear:
where the filter lives, whether empty means "all", which existing error mechanism
answers a bad value. Ask run 2 the counter-question too and it still names a
handful, sometimes more than run 1. So always ask again after the run, even
when your ticket was precise.

**Done when** the grid is complete and you can say which decisions run 1 made on
your behalf.

---

## H3 · Your own skill — 35 min

**Goal:** a working skill you can take to your own repo.

A recurring task from your own everyday work, or one of the two suggestions below.

```
Create a project skill for this repo: <the task>. Write the description first and
show it to me before you write the instructions.
```

Then commit.

**A · Characterization tests for a class.** Try it on `DepositCalculator` — you
will need those tests in H6 anyway. Check it works without naming the skill:
`Pin down what DepositCalculator does today so I can refactor it safely.`

**B · Clean up an instruction file.** Turn the two audit prompts from H1 into a
skill and try it on your own `AGENTS.md`. Check:
`My AGENTS.md has grown. Clean it up.`

### Done when the skill has triggered once on its own

**Restart Claude Code first**, otherwise the skill is invisible and
`/<skill-name>` answers "Unknown command". This repo has no `.claude/` directory,
so your first skill file creates it — and a directory that did not exist when the
session started is not watched.

Then type `/`: if the skill is in the list, its location and frontmatter are fine.
Now phrase the task in your own words, without naming the skill.

If it is missing from the list, the location or the frontmatter is wrong. If it is
in the list but does not trigger, the description is too weak — sharpen it rather
than adding more instructions.

---

## D1 · Subagent — 8 min, shown live

Nothing to build. What you see, so you can do it in your own repo:

A subagent is a file — `.claude/agents/<name>.md`. Its `tools:` line is the access
list: anything not named there is denied. Have one created rather than writing the
YAML yourself:

```
Create a project subagent money-audit that finds every place this service handles
money. Read-only: it may read, grep and glob, nothing else. It reports file and
line for every place an amount is stored, computed or returned.
```

Check the `tools:` line afterwards — "read-only" in the prompt does not guarantee a
narrow list. Then restart Claude Code, otherwise the new directory is not picked
up, and call it:

```
@money-audit find every place where this service handles money
```

The `@` forces the delegation instead of leaving it to the model.

Delegate when you want the result, not the path: searching and auditing yes,
changing and deciding no.

## D2 · Team configuration — 8 min, shown live

Nothing to build. Two files, same format, different purpose:

| File | Holds | In git |
|------|-------|--------|
| `.claude/settings.json` | what applies to everyone on the repo | yes |
| `.claude/settings.local.json` | what applies only to you | no, gitignored |

**This is configuration, not instruction.** `AGENTS.md` is read by the model, which
then follows it or does not. `settings.json` is read by Claude Code itself and
applied without asking the model — a hook runs, a `deny` rule holds, whatever the
model intends. That is the difference between asking for a standard and enforcing
one.

The shared one holds a hook that formats after every edit and a rule that stops the
agent from pushing. Have it created rather than writing the JSON yourself:

```
Create .claude/settings.json for this project with two things: a PostToolUse hook
on Edit and Write that runs the formatter, and a permission rule that denies
git push.
```

Check the result — it has to look like this:

```json
{
  "permissions": {
    "deny": ["Bash(git push)", "Bash(git push:*)"]
  },
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [{ "type": "command", "command": "./mvnw -q spotless:apply" }]
      }
    ]
  }
}
```

Two entries, two mechanisms. The **hook** runs `spotless:apply` after every edit —
a Maven goal that rewrites the files until they match the format configured in
`pom.xml`. Its sibling `spotless:check` only reports and fails the build, and that
one is bound to `verify`. A hook uses `apply`, because it should fix quietly rather
than break; `-q` keeps it from flooding the console. The **deny rule** takes `git
push` away from the agent, so nothing leaves the machine unless a human sends it.

Then the private one, with something that concerns nobody else:

```
Now create .claude/settings.local.json with a permission rule that only concerns
me: allow ./mvnw without asking.
```

And try it — have the agent attempt a push and watch the rule block it:

```bash
git push
```

Two things that silently do nothing: `allow` and `deny` must sit under
`permissions`, and a rule only covers the tool it names — `Bash(git push)` does not
stop a push issued through another shell tool. On Windows the hook command is
`mvnw.cmd`, not `./mvnw`.

`deny` beats `allow`, even a more specific one. A prohibition takes no exceptions.

## H6 · The big block — 45 min

**Goal:** one complete run, from ticket to reviewed result. Ticket:
[02-deposit-return.md](../issues/02-deposit-return.md).

### 1 · Choose model and reasoning effort

Deliberately, and say why.

### 2 · Have the code explained — and check the explanation

```
Read issues/02-deposit-return.md and explain the code it affects. Which classes
are involved and how do they fit together?
```

```
Show me the lines you based that on.
```

### 3 · Write the plan to a file

```
Write the plan to docs/plan-deposit-return.md: the three open questions from the
ticket, each with the decision and one sentence of reasoning, then the steps in
order. No code yet.
```

The first draft does not have to be right. It is a file, so it can be corrected,
reviewed by someone else, handed to a colleague, or picked up in a later session —
none of which works for a plan that only lives in the chat.

A file also keeps the context lean. Requirements that live in the chat get
restated turn after turn until the window is full of them. A file gets pointed at
once, and it frames the work instead of piling up inside it.

Have it reviewed, ideally from a fresh session:

```
Read docs/plan-deposit-return.md. What is wrong with it, what did it miss?
```

Feed the findings back into the file rather than rewriting it. Then commit.

Some decisions outlive the ticket — where the filter belongs, whether validation
rejects or clamps a bad value. An Architecture Decision Record holds such a
decision and the reasoning for it: why this way and not the alternatives. What
follows from it — the behaviour to build and to verify against — belongs in a spec,
and that spec is what you brief the agent with next time. The ADR answers why, the
spec answers what.

Writing either one out of a session is recurring work, which makes it a good
candidate for a skill of its own.

### 4 · Secure the existing behaviour

```
What does DepositCalculator do today for every packaging type and for unusual
quantities? Which of that is covered by tests?
```

Then have the tests written:

```
Pin down that behaviour in tests before anything changes. Do not touch the
production code.
```

Its own commit, before anything changes.

### 5 · Implement

```
Implement docs/plan-deposit-return.md.

Format: the implementation plus tests for the new behaviour, derived from the
acceptance criteria. Do not commit — I review first.
```

Context, goal and constraints are already in the ticket, `AGENTS.md` and the plan
— what is left to say is `Format`. Don't ask for a commit there, or the agent
commits before anyone has seen the result.

### 6 · Review your own result

Against the acceptance criteria, not against a first impression.

**Done when:**

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
```

is green and:

```bash
curl -X POST http://localhost:8080/api/returns -H "Content-Type: application/json" -d "{\"items\":[{\"productId\":\"P-1001\",\"quantity\":6}]}"
```

answers with `"totalDepositCents": 150`.
