# Desenvolvimento rastreável

O trabalho evolui em incrementos verticais, atômicos e reversíveis. Cada commit substantivo inclui exatamente um Journal em `journal/`, com objetivo, implementação, decisão arquitetural, comandos realmente executados e próximo passo.

Decisões duráveis ficam em `docs/adr/`; o desenho ativo fica em `docs/sdd/`. Mudanças comportamentais seguem red → green por bordas públicas declaradas em `docs/testing-seams.md`.

O gate `scripts/verify-traceability.sh` exige um Journal por commit após o bootstrap e impede Journals sem declaração de ADR.
