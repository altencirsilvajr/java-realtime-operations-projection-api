# 004 - Angular dashboard and delivery assets

## Commit

`feat(ui): add realtime operations dashboard`

## Objetivo

Permitir demonstrar localmente o fluxo completo e entregar build, containers, CI e manifests reproduzíveis.

## Implementacao

- Dashboard Angular 22 standalone com comandos, snapshot, timeline e reconexão STOMP.
- Imagens multi-stage, proxy Nginx, Docker Compose e configuração operacional.
- Manifests Kubernetes/OpenShift com ConfigMap, Secret externo, probes e recursos.
- README em PT-BR com arquitetura, execução e roteiro de entrevista.

## Rastreabilidade ADR

ADR aplicado: ADR-0001 - Persist projection and event before realtime publication.

## Verificacao

- `PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run build` — aprovado.
- `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw verify` — aprovado.
- `docker compose config --quiet` — aprovado.
- `docker compose build` — imagens da API e do frontend construídas.
- `docker compose up -d` com curls de health e painel — smoke test aprovado em `8080` e `5408`.
- `./scripts/verify-traceability.sh` e `git diff --check` — aprovados.

## Alternativas e trade-offs

O cliente recebe um sinal STOMP e relê o snapshot em vez de confiar no payload; há uma leitura adicional, mas reconexão e atualização usam a mesma fonte de verdade.

O workflow de CI validado localmente foi preservado no commit `db4d5f9` da branch local `workflow-ci-local`; o token OAuth recusou publicá-lo sem o escopo `workflow`.

## Proximo passo

Usar o roteiro do README para demonstração em entrevistas.
