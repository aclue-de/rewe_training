# Setup

Do this before the training starts, not during it. Twenty minutes, and the check
at the end tells you whether it worked.

## JDK 21

Any distribution will do — Temurin, Corretto, Zulu, the one your team already
uses. Version 21 exactly: the project is built against it, and 17 will not
compile it.

```bash
java -version
```

Expected: a line starting with `openjdk version "21`.

If several JDKs are installed, `JAVA_HOME` decides which one Maven picks.

## IntelliJ IDEA

Community Edition is enough. Use 2023.3 or newer — older versions do not know
Java 21.

After **File | Open** on this folder, IDEA reads `pom.xml` and downloads the
dependencies by itself. Set the SDK under **File | Project Structure | Project**
to 21 if IDEA does not find it.

## Claude Code CLI

Needs Node.js 18 or newer.

```bash
npm install -g @anthropic-ai/claude-code
claude --version
```

Then start it once inside the project folder and sign in:

```bash
claude
```

Documentation: <https://docs.claude.com/en/docs/claude-code>

## Git

Needed to clone this repository. Any version.

```bash
git --version
```

## Docker — optional

Only needed if you want to run the service without a local JDK. Docker Desktop
brings both parts.

```bash
docker --version
docker compose version
```

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
