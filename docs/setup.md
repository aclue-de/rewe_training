# Setup

Do this before the training starts, not during it. Twenty minutes, and the check
at the end tells you whether it worked.

Three things are required: **a JDK 21, the Claude Code CLI and Git.** IntelliJ and
Docker are optional. Maven is not needed — `mvnw` downloads it.

Without a JDK there is a fallback that runs the build in a container — see
[Docker](#docker--optional) — but it is slower and needs one extra line in your
`AGENTS.md`.

## JDK 21 — required

Any distribution will do — Temurin, Corretto, Zulu, the one your team already
uses. Version 21 exactly: the project is built against it, and 17 will not
compile it.

```bash
java -version
```

Expected: a line starting with `openjdk version "21`.

If several JDKs are installed, `JAVA_HOME` decides which one Maven picks.

## IntelliJ IDEA — optional

Only if you would rather work in an IDE than in the terminal. Community Edition is
enough. Use 2023.3 or newer — older versions do not know Java 21.

After **File | Open** on this folder, IDEA reads `pom.xml` and downloads the
dependencies by itself. Set the SDK under **File | Project Structure | Project**
to 21 if IDEA does not find it.

## Claude Code CLI — required

The native installer is the recommended way — it keeps itself up to date in the
background. No Node.js needed.

**Windows PowerShell:**

```powershell
irm https://claude.ai/install.ps1 | iex
```

**macOS, Linux, WSL:**

```bash
curl -fsSL https://claude.ai/install.sh | bash
```

**With Homebrew instead:**

```bash
brew install --cask claude-code
```

Homebrew does not auto-update — `brew upgrade claude-code` when you want a newer
version.

Then check it and sign in. Starting `claude` opens a browser for the login:

```bash
claude --version
claude
```

Needs a Pro, Max, Team or Enterprise account; the free plan does not include
Claude Code. If something looks wrong, `claude doctor` prints diagnostics without
starting a session.

On native Windows, install [Git for Windows](https://git-scm.com/downloads/win) as
well — with it Claude Code uses Git Bash for shell commands, without it PowerShell.

Documentation: <https://code.claude.com/docs/en/setup>

## Git — required

For cloning the repository and for the branch you work on. Any version.

```bash
git --version
```

## Docker — optional

For running the service in a container. Docker Desktop brings both parts.

```bash
docker --version
docker compose version
```

`docker compose up --build` starts the service, but its image build skips the
tests — so it is no substitute for the build.

### If you cannot install a JDK

There is a second service that runs the build in a container:

```bash
docker compose run --rm build
```

Same result as `./mvnw verify`, no local JDK needed. Dependencies are cached in a
volume, so only the first run is slow.

Two things to know before you rely on it. It is noticeably slower than a local
build, especially on Windows, where a Maven build reads thousands of files across
a bind mount. And Claude Code runs on your machine, not in the container — when
it writes tests and wants to run them, it will reach for `./mvnw` unless your
`AGENTS.md` tells it to use this command instead.

A local JDK is the smoother path. Use this one only if you have no choice.

## Maven — not needed

`mvnw` downloads Maven on first use, into your user directory, not into the
project. There is no `maven-wrapper.jar` in this repository and there should
never be one.

## The check

```bash
./mvnw verify
```

Expected: `Tests run: 13, Failures: 0, Errors: 0` and `BUILD SUCCESS`. The first
run takes a few minutes because it downloads Maven and the dependencies; every
run after that takes seconds.

Then start the service and open <http://localhost:8080>:

```bash
./mvnw spring-boot:run
```

## When it does not work

**`java: invalid target release: 21`** — IDEA or Maven is using an older JDK.
Check `java -version` and the SDK in **Project Structure**.

**Port 8080 is already in use** — start it somewhere else:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

With Docker, change the left half of the port mapping in `compose.yaml`.

**`mvnw` cannot download Maven** — usually a corporate proxy. Configure it in
`~/.m2/settings.xml`, or work in IDEA, which brings its own Maven.

**The Docker build takes forever the first time** — it downloads the
dependencies inside the image. The second build reuses that layer as long as
`pom.xml` stays unchanged.
