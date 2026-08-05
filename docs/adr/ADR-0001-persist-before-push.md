# ADR-0001 - Persist projection and event before realtime publication

## Status

Accepted

## Contexto

Clientes podem desconectar ou perder frames. Publicar antes do commit permitiria observar uma versão que não existe na fonte de leitura ou anunciar uma transação posteriormente revertida.

## Decisao

Evento append-only e projeção são gravados na mesma transação PostgreSQL. A aplicação publica um evento interno e o adaptador STOMP só o trata em `AFTER_COMMIT`. Reconexão sempre usa o snapshot HTTP.

## Consequencias

- Push nunca precede a versão consultável.
- Falha do broker após commit não reverte o estado; clientes recuperam por snapshot.
- Entrega durável de notificações exigiria outbox/broker externo, fora do escopo deste laboratório.

## Alternativas rejeitadas

### Publicar dentro da transação

Rejeitada porque expõe estado não confirmado e acopla consistência do domínio ao transporte.

### Usar WebSocket como fonte de verdade

Rejeitada porque reconexão e frames perdidos tornariam o estado incompleto.
