# 002 - Operation state machine

## Commit

`feat(domain): model operation state transitions`

## Objetivo

Registrar operações e impedir transições inválidas por uma API de domínio independente de framework.

## Implementacao

- Build Maven reproduzível para Java 21 e Spring Boot 3.5.
- Agregado com estados `CREATED`, `PROCESSING`, `COMPLETED` e `FAILED` e eventos sequenciais.
- Testes de comportamento escritos antes da implementação.

## Rastreabilidade ADR

Decisao local sem ADR novo: a máquina de estados é regra explícita do domínio e facilmente evolutiva.

## Verificacao

- Teste focado inicialmente falhou por tipos de domínio ausentes (red).
- `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw test` — aprovado após implementação (green).
- `./scripts/verify-traceability.sh` e `git diff --check` — aprovados.

## Alternativas e trade-offs

Uma biblioteca de state machine foi rejeitada: quatro estados não justificam ocultar a regra central do laboratório.

## Proximo passo

Persistir evento e projeção e expor comandos e snapshot.
