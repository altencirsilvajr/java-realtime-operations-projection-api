# Java Realtime Operations Projection API

Laboratório vertical Java 21 + Spring Boot 3.5 que demonstra uma projeção operacional persistida com atualizações STOMP pós-commit e recuperação por snapshot HTTP. Foi desenhado para estudo e discussão em entrevistas Senior, não como produto genérico.

## O que este projeto prova

- A máquina de estados `CREATED → PROCESSING → COMPLETED|FAILED` pertence ao domínio, sem dependências Spring.
- Cada mudança gera um evento append-only e atualiza uma projeção PostgreSQL na mesma transação.
- `@TransactionalEventListener(AFTER_COMMIT)` impede que o WebSocket anuncie estado não confirmado.
- STOMP não é fonte de verdade: após reconectar, o cliente recupera timeline e versão por HTTP.
- ArchUnit protege domínio e transporte; Testcontainers exercita PostgreSQL, REST e WebSocket reais.

## Executar localmente

Requer Docker. Para executar toda a solução:

```bash
docker compose up --build
```

- Painel Angular (Compose): <http://localhost:5408>
- OpenAPI: <http://localhost:8080/swagger-ui.html>
- Health: <http://localhost:8080/actuator/health>
- Métricas Prometheus: <http://localhost:8080/actuator/prometheus>

Sem containers para o código da aplicação:

```bash
docker compose up -d postgres
JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw spring-boot:run
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend start
```

## Fluxo para demonstração

```bash
curl -i -X POST http://localhost:8080/api/operations \
  -H 'Content-Type: application/json' \
  -d '{"name":"Import payroll"}'

curl -i -X POST http://localhost:8080/api/operations/OPERATION_ID/transitions \
  -H 'Content-Type: application/json' \
  -d '{"status":"PROCESSING"}'

curl http://localhost:8080/api/operations/OPERATION_ID
```

O tópico geral é `/topic/operations`; cada operação também é publicada em `/topic/operations/{id}`. O endpoint WebSocket/STOMP é `/ws`.

## Arquitetura e consistência

O controller traduz HTTP para a aplicação. `OperationService` reidrata o agregado, aplica a regra, persiste evento e projeção e emite um evento interno. O adaptador STOMP só recebe esse evento após o commit. Se o processo cair entre commit e push, o snapshot segue correto; entrega durável de notificações exigiria outbox e broker externo, trade-off intencional descrito no ADR.

Concorrência usa optimistic locking da projeção. A constraint `(operation_id, sequence)` impede eventos duplicados na timeline. Problem Details inclui `correlationId`; Actuator oferece health probes e métricas.

## Verificação

```bash
./scripts/verify-traceability.sh
JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw verify
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend ci
PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run build
docker compose config --quiet
```

Os manifests em `deploy/k8s` usam ConfigMap, Secret externo, probes e limites. `deploy/openshift/route.yaml` adiciona a Route para OpenShift. Crie o Secret `realtime-operations-db` fora do Git.

## Rastreabilidade

- Visão: `PROJECT_VISION.md`
- SDD ativo: `docs/sdd/active.md`
- Decisões: `docs/adr/`
- Evidência incremental: `journal/`
- Seams de teste: `docs/testing-seams.md`
