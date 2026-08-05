# Repository Instructions

This repository uses tracked development. Every substantive non-merge commit must update exactly one file under `journal/` and declare its ADR status.

## Required verification

```bash
./scripts/verify-traceability.sh
JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw verify
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend ci
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run test:ci
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run build
```

Run `git diff --check` before every commit. Keep production code and commit messages in English; write documentation in Brazilian Portuguese.
