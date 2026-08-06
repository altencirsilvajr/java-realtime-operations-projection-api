# 007 - Endurecer toolchain de CI

## Commit

`ci: eliminate toolchain warnings`

## Objetivo

Remover os alertas da cadeia Angular e os avisos de Actions legadas.

## Implementacao

- Fixa `@hono/node-server` corrigido em 2.1.0.
- Registra allowlist versionada dos scripts de instalacao.
- Atualiza Actions para Node 24 e adiciona audit ao gate frontend.

## Rastreabilidade ADR

Decisao local sem ADR novo: manutencao reversivel sem alterar a projecao realtime.

## Verificacao

- `npm audit`: 0 vulnerabilidades e nenhum script pendente.
- Teste frontend: 1 aprovado; build Angular aprovado.
- Workflow validado como YAML e sem Actions antigas.
