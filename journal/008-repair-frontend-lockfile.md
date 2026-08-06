# 008 - Reparar lockfile do frontend

## Commit

`fix: synchronize frontend lockfiles`

## Objetivo

Restaurar o lockfile completo do console realtime.

## Implementacao

- Recupera a copia integral preservada e sincroniza seu metadata com npm 11.17.

## Rastreabilidade ADR

Decisao local sem ADR novo: reparo de dependencias sem alterar eventos ou projecoes.

## Verificacao

- Lockfile JSON valido; `npm ci` sem warnings.
- Audit: 0 vulnerabilidades; nenhum script pendente.
