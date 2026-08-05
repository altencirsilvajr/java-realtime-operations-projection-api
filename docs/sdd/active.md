# SDD ativo — laboratório de operações em tempo real

## Fluxo vertical

1. `POST /api/operations` registra uma operação em `CREATED`.
2. `POST /api/operations/{id}/transitions` solicita uma transição válida.
3. A aplicação grava evento e projeção na mesma transação PostgreSQL.
4. Um evento pós-commit publica o snapshot em `/topic/operations/{id}`.
5. `GET /api/operations/{id}` restaura o snapshot completo após reconexão.

## Restrições

- O adaptador STOMP não decide nem altera estado.
- A timeline é ordenada por sequência persistida.
- Falhas HTTP usam Problem Details e carregam correlation ID.
