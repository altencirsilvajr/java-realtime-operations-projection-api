# Realtime Operations Projection API

Laboratório vertical Java para demonstrar mudanças operacionais quase em tempo real sem colocar regras de negócio no transporte WebSocket.

O fluxo registra uma operação, valida transições no domínio, persiste evento e projeção de leitura no PostgreSQL e publica a atualização STOMP somente após o commit. Clientes reconectados recuperam a verdade persistida por snapshot HTTP.

## Critérios de sucesso

- Regras de transição pertencem ao domínio/aplicação.
- Eventos operacionais são append-only e a leitura usa uma projeção persistida.
- Nenhum push é observável antes da persistência.
- Reconexão recupera o estado por snapshot HTTP.
- API, interface Angular, observabilidade e execução local são reproduzíveis.
