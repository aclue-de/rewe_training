# Prompts for an instruction file

Six prompts for keeping an `AGENTS.md` honest. The first two are the ones you need
after `/init`; the rest are for a file that has grown over months. The reasoning
behind them is in [writing-agents-md.md](writing-agents-md.md).

Two rules apply to all of them.

**Cap the output.** Append this to every prompt:

> at most five findings, one line each, worst first, no prose. Then stop — do not
> edit anything.

Without it you get an essay, and reading it costs more than the fix.

**Never rewrite in one pass.** Have it propose, decide yourself, then apply. This
file governs every later session, so a silent mistake in it outlives any mistake
in the code.

---

## 1 · Origin and expiry

**Helps with:** rules that only a closed ticket justifies, and lines that stop
being true once the current work is done. This is the single most useful prompt —
run it first.

```
Audit this instruction file. For each rule ask two things: does it exist because
of how this codebase is built, or because of one ticket or task? And would it
still be true once the current work is finished?
```

Expect it to end in a question you have to answer: is this rule house style, or
was it only ever meant for that one ticket? Nothing in the code answers that.

## 2 · The gap

**Helps with:** conventions the codebase follows consistently but states nowhere —
the ones `/init` cannot find, and the reason the file is worth keeping.

```
Ignore this file and read the code instead. Name up to five conventions this
codebase follows that the file does not mention. One line each, with one
file:line where the convention is visible.
```

The `file:line` is not decoration. Without a place to check, you get plausible
inventions phrased with total confidence — and once written, they stay.

## 3 · Rule or observation

**Helps with:** sentences describing what the code happens to look like right now.
They read like guidance and expire without anyone noticing.

```
Which statements in this file describe what the code happens to look like right
now, rather than a rule you want followed? Rewrite those as rules, or drop them
if there is no rule behind them.
```

Watch for "currently", "today", "at the moment". Not every observation has a rule
behind it — those get deleted, not rephrased.

## 4 · Redundancy

**Helps with:** copies of facts that live somewhere else and will drift apart.

```
Which parts of this file repeat information that already lives in build config,
generated documentation, formatter config or CI? List them with the single source
of truth for each.
```

**Handle its output carefully** — taken literally this prompt guts the file. Build
commands do follow from the build config, and that is exactly why they belong
here: without them every session starts by reading it. Ask first: *would a wrong
copy cause damage, and would anyone notice?* Configured values fail that test.
Commands and reasoning pass it.

## 5 · The cold read

**Helps with:** gaps you cannot see as the author, because you fill them in from
memory.

```
Pretend you have never seen this repository and have nothing but this file. Name
the five decisions you would most likely get wrong on your first change here.
```

## 6 · Cut without loss

**Helps with:** length. Every line is read in every session, so a rule that has
never changed a decision competes with the ones that matter.

```
Cut this file by a third without losing a single rule. Report what you removed
and why it was safe.
```

If the file resists cutting, that is the signal to split it by topic and pull the
parts in with `@` imports instead.
