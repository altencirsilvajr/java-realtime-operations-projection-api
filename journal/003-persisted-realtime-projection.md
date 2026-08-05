# 003 - Persisted realtime projection

## Commit

`feat(api): persist projections before realtime push`

## Objetivo

Expor comandos, snapshot e notificações em tempo real com evento e projeção persistidos antes de qualquer push.

## Implementacao

- API REST com validação, OpenAPI, Problem Details e correlation ID.
- Evento append-only e projeção de leitura em PostgreSQL via JPA/Hibernate e Flyway.
- Publicador STOMP pós-commit sem regra de negócio.
- Testes reais de PostgreSQL, HTTP, STOMP, ordering, reconexão e arquitetura.

## Rastreabilidade ADR

Novo ADR criado: ADR-0001 - Persist projection and event before realtime publication.

## Verificacao

- Teste de integração inicialmente falhou sem configuração Spring Boot (red).
- `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw verify` — aprovado com Testcontainers (green).
- `./scripts/verify-traceability.sh` e `git diff --check` — aprovados.

## Alternativas e trade-offs

Um broker externo seria mais resiliente, mas o broker STOMP simples mantém o laboratório local; a recuperação não depende dele porque o snapshot persistido é a verdade.

## Proximo passo

Adicionar cliente Angular, empacotamento, CI e documentação operacional.
