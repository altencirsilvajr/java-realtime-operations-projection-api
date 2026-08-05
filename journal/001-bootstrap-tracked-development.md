# 001 - Bootstrap tracked development

## Commit

`chore: bootstrap tracked development`

## Objetivo

Estabelecer a visão, as bordas públicas de teste e o processo auditável antes do código de produto.

## Implementacao

- Criados instruções do repositório, SDD ativo, visão, seams de teste e gate executável.

## Rastreabilidade ADR

Decisao local sem ADR novo: o processo de entrega é governança reversível e não uma decisão da arquitetura do produto.

## Verificacao

- `./scripts/verify-traceability.sh` — gate aprovado no bootstrap.
- `git diff --check` — sem erros.

## Alternativas e trade-offs

O bootstrap separado adiciona um commit, mas torna todo o código posterior rastreável desde a origem.

## Proximo passo

Adicionar build reproduzível e máquina de estados test-first.
