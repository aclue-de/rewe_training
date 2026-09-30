# Playbook

What to do in each hands-on block: the goal, the prompts, the commands, and how
you know you are finished.

> **Understanding the repo is not the goal.** That applies to every block. The
> practice project is only the occasion to work with Claude Code. At times that
> feels unsatisfying, and that is intentional.

**Getting through the list is not the point either.** You should be able to say
afterwards which decisions were yours and which the AI made. If you run out of
time on that, that is fine.

| Block | Time | Goal | Result |
|-------|------|------|--------|
| **H1** First session | 35 min | give Claude Code the project context | `AGENTS.md` |
| **H2** Right-sizing and anti-patterns | 30 min | see that decisions get made even when you don't make them | your numbers in the Miro table |
| **H3** Your own skill | 35 min | don't re-explain what recurs | `SKILL.md` |
| **H4** Subagent | 8 min | hand off work with limited permissions | `.claude/agents/money-audit.md` |
| **H5** Team configuration | 8 min | enforce rules instead of asking for them | `.claude/settings.json` |
| **H6** The big block | 45 min | implement a ticket to a plan in reviewable steps | plan file and one commit per step |

The tickets are in [../issues](../issues), the setup in [setup.md](setup.md), the
API in [api.md](api.md).

## Before you start

Teams of two or three. One branch per team, off `main`, for the whole day.

```bash
git switch -c training main
./mvnw -B -ntp clean verify        # no local JDK: docker compose run --rm verify
```

Expected: `Tests run: 13, Failures: 0` and `BUILD SUCCESS`. Don't use `-q`. The
`WARN` about `must not be empty` is not an error.

Without a local JDK, `docker compose run --rm verify` replaces every `./mvnw`
line. Put that command in your `AGENTS.md` in H1.

Commit after *every* block.

---

## H1 · First session — 35 min

**Goal:** you give Claude Code the project context. The draft from `/init`
becomes an `AGENTS.md`.

### 1 · Run `/init` in Claude Code

```
/init
```

It writes a `CLAUDE.md`. You don't need to read all of it.

### 2 · Remove what does not belong

```
Audit this instruction file. For each rule ask two things: does it exist because
of how this codebase is built, or because of one ticket or task? And would it
still be true once the current work is finished?

At most five findings, one line each, worst first, no prose. Then stop — do not
edit anything.
```

The limit of five findings is deliberate, here and in the next step: you are not
meant to spend hours understanding the code. The most important findings are
enough.

Then tell Claude which entries to remove.

### 3 · Add what is missing

```
Which rules does the code follow consistently that are not yet in the CLAUDE.md?
For example on naming, package structure, error handling or tests. Derive them
from the code, not from the CLAUDE.md. At most five, one line each with a
file:line where the rule is visible. Short and clear, facts only. Don't change
anything yet.
```

Then tell Claude which of them to add to the `CLAUDE.md`. Never have the whole
file rewritten in one pass.

Without a local JDK, also have it add that the build runs with
`docker compose run --rm verify`.

### 4 · Rename and commit

At REWE the file is called `AGENTS.md`, the `CLAUDE.md` only imports it. Run this
in the normal terminal, not as a prompt:

```bash
mv CLAUDE.md AGENTS.md
echo "@AGENTS.md" > CLAUDE.md
git add -A && git commit -m "Add AGENTS.md"
```

**Done when:**

- `AGENTS.md` exists and `CLAUDE.md` imports it
- it holds rules that `/init` did not find on its own
- no line in it would be false once today's work is finished

---

## H2 · Right-sizing and anti-patterns — 30 min

**Goal:** you see that decisions get made even when you don't make them yourself.
To show it, Claude implements the same ticket twice.
Ticket: [01-products-filterable.md](../issues/01-products-filterable.md).

You don't read code and you don't judge a solution. What you compare are the
numbers.

### How to proceed

1. **Secure your state:** everything from H1 is committed.
2. **Run 1:** hand over the ticket as it stands and just have it done.
3. **Fill in the "Run 1" column:** top to bottom, as in the table.
4. **Reset everything:** clear the context and the working tree.
5. **Run 2:** first have the AI name all open points with a recommendation and
   turn them into a brief. You adopt the recommendations as your decisions, then
   Claude implements the brief.
6. **Fill in the "Run 2" column:** top to bottom, as in run 1.
7. **Enter it in Miro:** both columns go into the table on the Miro board, so we
   can compare the teams' results.

No branch switching. Fill in each column while that run is still there.

| | Run 1 | Run 2 |
|---|---|---|
| Files / lines *(insertions / deletions)* | | |
| Decisions the AI made for you *(count)* | | |
| of those, written down *(count)* | | |
| `verify` green? *(yes / no · number of tests)* | | |

### Secure your state

Resetting after run 1 deletes everything that is not committed, including an
uncommitted `AGENTS.md` from H1. So before run 1:

```bash
git status --short
```

The output must be empty. Otherwise commit first:

```bash
git add -A && git commit -m "Add AGENTS.md"
```

### Run 1 — 10 min

```
Implement what issues/01-products-filterable.md asks for. Just get it done.
```

Don't intervene. If Claude asks back, answer: "Decide for yourself."

Then fill in the column from top to bottom.

**Files / lines:**

```bash
git add -A && git --no-pager diff --cached --stat
```

`git add -A` makes sure newly created files are counted too.

**Decisions and of those written down:**

```
Which decisions did you make during the implementation that were specified
neither in the ticket nor in a brief I approved? One short line per decision:
what you decided and where it is written down, otherwise "nowhere". Written down
means explicitly described, for example in a comment, a test or documentation.
At the end, two numbers: decisions in total, of those written down.
Short and clear, facts only. No additional or explanatory text.
```

If the answer is long anyway, just note the two numbers.

**`verify` green? · Number of tests:**

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
```

Note green or red and the number from the `Tests run:` line under `Results:`.
Red is a finding, not a reason to stop.

Then clear the context and the working tree. Only run this if you secured your
state before run 1:

```
/clear
```

```bash
git reset --hard && git clean -fd
```

### Run 2 — 10 min

```
Don't write any code yet. Which points does issues/01-products-filterable.md
leave open? List all open points, each with your recommendation in one line.
Turn them into a brief in four building blocks: context, goal, constraints,
format. Short and clear, facts only.
```

You adopt the recommendations without judging them. That makes them your
decisions. In production code you would judge them, here there is no time for
that.

```
Adopt all your recommendations and implement the brief.
```

Then fill in the "Run 2" column from top to bottom, as in run 1.

**Files / lines:**

```bash
git add -A && git --no-pager diff --cached --stat
```

**Decisions and of those written down:** the same question as in run 1, word for
word, so the numbers stay comparable.

```
Which decisions did you make during the implementation that were specified
neither in the ticket nor in a brief I approved? One short line per decision:
what you decided and where it is written down, otherwise "nowhere". Written down
means explicitly described, for example in a comment, a test or documentation.
At the end, two numbers: decisions in total, of those written down.
Short and clear, facts only. No additional or explanatory text.
```

If the answer is long anyway, just note the two numbers.

**`verify` green? · Number of tests:**

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
```

Note green or red and the number from the `Tests run:` line under `Results:`.

Once the column is filled in, commit:

```bash
git add -A && git commit -m "Filter the product list by packaging type"
```

### Enter it in Miro — 10 min

Enter both columns in the table on the Miro board. We discuss them together once
every team has entered theirs.

**Done when** both columns are in Miro.

---

## H3 · Your own skill — 35 min

**Goal:** a skill that triggers on its own and that you can take to your own repo.

Which skill is entirely your choice. Ideally a task you explain or do again and
again in your everyday work, even with no connection to this repo. If you have no
idea of your own, take one of the suggestions A to C below.

```
Create a project skill for this repo: <the task>. Show me the description first,
before you write the instructions.
```

Then commit.

**Suggestions, if you have no idea of your own:**

**A · Characterization tests for a class.** Try it on `DepositCalculator`, you
will need those tests in H6. Test without naming the skill:
`Pin down what DepositCalculator does today so I can refactor it safely.`

**B · Clean up an instruction file.** Turn the two audit prompts from H1 into a
skill. Test: `My AGENTS.md has grown. Clean it up.`

**C · Disclose decisions.** Turn the question about decisions from H2 into a
skill that, after a change, lists what was decided without being specified and
where it is written down. Test on the last commit from H2:
`What is in the last commit that the ticket did not specify?`

### Try it out

This repo has no `.claude/skills/` at the start, so Claude Code does not pick up
new skills there on its own. After creating the skill and after every change to
it:

```
/reload-skills
```

Then type `/`. If the skill is in the list, phrase the task in your own words,
without naming the skill.

If it is missing from the list, the location or the frontmatter is wrong. If it
does not trigger, sharpen the description rather than adding more instructions.

**Done when** the skill has triggered once on its own.

---

## H4 · Subagent — 8 min

**Goal:** you create a subagent with limited permissions and call it explicitly.

The `tools:` line in `.claude/agents/<name>.md` is the access list: anything not
named there is denied.

```
Create a project subagent money-audit that finds every place this service handles
money. Read-only: it may read, grep and glob, nothing else. It reports file and
line for every place an amount is stored, computed or returned.
```

Check the `tools:` line: only `Read, Grep, Glob`. Then restart Claude Code and
call it:

```
@money-audit find every place where this service handles money
```

The `@` forces the delegation.

Delegate when you want the result, not the path: searching and auditing yes,
changing and deciding no.

```bash
git add .claude/agents && git commit -m "Add money-audit subagent"
```

**Done when:**

- the `tools:` line holds only read-only tools
- the call returned a list with file and line
- the file is committed

---

## H5 · Team configuration — 8 min

**Goal:** a `settings.json` with a hook and a block on `git push` that apply
whatever the model intends.

| File | Holds | In git |
|------|-------|--------|
| `.claude/settings.json` | what applies to everyone on the repo | yes |
| `.claude/settings.local.json` | what applies only to you | no, gitignored |

The model follows `AGENTS.md` or it doesn't. Claude Code enforces `settings.json`
itself.

### 1 · Have the shared file created

```
Create .claude/settings.json for this project with two things: a PostToolUse hook
on Edit and Write that runs the formatter, and a permission rule that denies
git push.
```

Check the result:

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

On Windows `mvnw.cmd` instead of `./mvnw`. Without a local JDK
`docker compose run --rm verify mvn -q spotless:apply`.

### 2 · Have the private file created

```
Now create .claude/settings.local.json with a permission rule that only concerns
me: allow ./mvnw without asking.
```

### 3 · Try it out

The settings take effect without a restart. Trigger the hook:

```
Add a comment to TrainingApplication.java, deliberately indented wrong.
```

`git diff` shows the comment properly indented. Then discard it:

```bash
git restore src/main
```

Then have it attempt a push:

```
Run git push.
```

Claude Code has to refuse without asking. If it asks, the rule is not working:
answer no and check that `deny` sits under `permissions`.

### 4 · Commit

```bash
git add .claude/settings.json && git commit -m "Add shared Claude Code settings"
```

**Done when** the hook has reformatted the file, the push was refused and only
`settings.json` is committed.

---

## H6 · The big block — 45 min

**Goal:** you implement a larger ticket in small steps. First, with the right
model and effort, a plan is written to a file. Then each step is implemented on
its own, tested by you and committed, and the context is cleared afterwards.
Ticket: [02-deposit-return.md](../issues/02-deposit-return.md).

Here too you don't need to understand the code. You check whether each step does
what the plan says and whether the build stays green.

### How to proceed

1. **Choose model and effort** for the planning.
2. **Have the plan written:** with rules for implementation, cut into steps.
3. **Have the plan reviewed**, commit, clear the context.
4. **Each step on its own:** choose model and effort, have it implemented and the
   plan updated, test it yourself, commit, clear the context. Until the plan is
   done.
5. **Sign-off:** against the acceptance criteria.

### 1 · Choose model and effort for the planning

Deliberately, and say why.

```
/model
```

```
/effort
```

### 2 · Have the plan written to a file

```
Read issues/02-deposit-return.md and write a plan to docs/plan-deposit-return.md.
No code yet.

Structure of the plan:
1. Rules for implementation that apply to every step:
   - implement only the step named
   - afterwards update this plan: tick off the step, record deviations from the
     plan and new decisions with one sentence of reasoning
   - the build is green afterwards
   - do not commit
   - in the chat only: changed files and how I test the step, short and clear
2. The three open questions from the ticket, each with the decision and one
   sentence of reasoning.
3. At most five steps in order. Each can be reviewed, tested and committed on its
   own, and the build is green afterwards. Per step: goal, affected files, how I
   test it, recommended model and effort.

In the chat only: where the plan is and how many steps it has.
```

The rules are in the plan so they still apply after every `/clear`.

### 3 · Have the plan reviewed

Have it reviewed with an empty context:

```
/clear
```

```
Read docs/plan-deposit-return.md. What is wrong with it, what is missing? Can
each step be reviewed, tested and committed on its own? Are rules for
implementation missing? At most five findings, one line each. Short and clear,
facts only. Don't change anything yet.
```

The limit of five findings is deliberate: you are not meant to spend hours
understanding the code. The most important findings are enough.

Tell Claude which findings to work into the file. Don't have it rewritten. Then
commit and clear the context:

```bash
git add docs/plan-deposit-return.md && git commit -m "Add plan for the deposit return"
```

```
/clear
```

### 4 · Implement step by step

For each step in the plan, in order:

**Choose model and effort.** Deliberately, the recommendation in the plan is the
starting point.

```
/model
```

```
/effort
```

**Have it implemented:**

```
Implement step <n> of docs/plan-deposit-return.md. Follow the rules for
implementation in the plan.
```

**Test it yourself,** as the plan says for this step, and always:

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
```

If `verify` is red, have Claude fix it before you commit.

Also check that the plan is updated: step ticked off, deviations and new
decisions recorded. Otherwise remind Claude.

**Commit,** code and updated plan together:

```bash
git add -A && git commit -m "<what the step does>"
```

**Clear the context:**

```
/clear
```

Then the next step, until the plan is done.

### 5 · Sign-off

```
Check the implementation against the acceptance criteria in
issues/02-deposit-return.md. One line per criterion: met or not, with evidence as
file:line or test name. Short and clear, facts only.
```

**Done when** every step in the plan is ticked off and committed,

```bash
./mvnw -B -ntp verify              # no local JDK: docker compose run --rm verify
```

is green, and with the service running

```bash
./mvnw spring-boot:run             # no local JDK: docker compose up --build
```

this, in a second terminal,

```bash
curl -X POST http://localhost:8080/api/returns -H "Content-Type: application/json" -d "{\"items\":[{\"productId\":\"P-1001\",\"quantity\":6}]}"
```

answers with `"totalDepositCents": 150`.
