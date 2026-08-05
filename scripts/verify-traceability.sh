#!/usr/bin/env bash
set -euo pipefail

root="$(git rev-parse --show-toplevel)"
cd "$root"

test -f AGENTS.md
test -f DEVELOPMENT.md
test -f docs/sdd/active.md
test -f docs/testing-seams.md

for journal in journal/*.md; do
  grep -Eq 'Novo ADR criado:|ADR aplicado:|Decisao local sem ADR novo:' "$journal" || {
    echo "Missing ADR declaration in $journal" >&2
    exit 1
  }
done

if git rev-parse HEAD^ >/dev/null 2>&1; then
  count="$(git diff --name-only HEAD^ HEAD -- journal/ | wc -l | tr -d ' ')"
  test "$count" = "1" || {
    echo "Expected exactly one journal changed in the latest commit, found $count" >&2
    exit 1
  }
fi

echo "Traceability gate passed."
