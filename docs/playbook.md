# Playbook

What to do in each hands-on block: the goal, the prompts to copy, the commands,
and how you know you are finished. Two blocks are live demos — you watch those.

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
git switch -c training main && ./mvnw -B -ntp clean verify
```

Expected: `Tests run: 13, Failures: 0` and `BUILD SUCCESS`. Don't use `-q`, it
hides both lines. The `WARN` about `must not be empty` during the tests is not an
error — it is the test for the 400 response.

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

More on the criteria in [writing-agents-md.md](writing-agents-md.md), and the
prompts for later use in [agents-md-prompts.md](agents-md-prompts.md).

---

## H2 · Right-sizing — 30 min

**Goal:** the same task twice — once handed over as it stands, once properly
briefed — and the two results compared. Ticket:
[01-products-filterable.md](../issues/01-products-filterable.md).

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
it is written down.
```

### Run 1 — 10 min

```
Implement what issues/01-products-filterable.md asks for. Just get it done.
```

Look at it and fill the left column:

```bash
./mvnw -B -ntp verify
git --no-pager diff --stat
git --no-pager diff -- src
```

A red `verify` here is a finding, not a reason to stop. Then discard:

```bash
git checkout . && git clean -fd
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
- ./mvnw verify is green

Format: a diff plus tests — including one that checks the error path returns a
ProblemDetail body, not just the right status.
```

Conventions do not belong in the brief, they are in `AGENTS.md`. Leave one of the
four blocks out and the agent guesses at exactly that point.

Fill the right column, then keep it:

```bash
./mvnw -B -ntp verify
git add -A && git commit -m "Filter the product list by packaging type"
```

### Compare — 10 min

The grid is filled. Put the list of open questions from the counter-question next
to it and go through them one by one.

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

**B · Clean up an instruction file.** Turn the prompts from
[agents-md-prompts.md](agents-md-prompts.md) into a skill and try it on your own
`AGENTS.md`. Check: `My AGENTS.md has grown. Clean it up.`

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

A hook that formats after every edit, and a rule that stops the agent from
pushing:

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

Then try it — have the agent attempt a push and watch the rule block it:

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

Have the plan reviewed, ideally from a fresh session:

```
Read docs/plan-deposit-return.md. What is wrong with it, what did it miss?
```

Then commit.

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
./mvnw -B -ntp verify
```

is green and:

```bash
curl -X POST http://localhost:8080/api/returns -H "Content-Type: application/json" -d "{\"items\":[{\"productId\":\"P-1001\",\"quantity\":6}]}"
```

answers with `"totalDepositCents": 150`.
