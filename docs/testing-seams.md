# Bordas públicas de teste

- Domínio: registro e transição pela API pública de `Operation`.
- HTTP: comandos REST e snapshot, incluindo Problem Details.
- Tempo real: atualização STOMP observável somente depois que o snapshot persistido já pode ser lido.
- Reconexão: novo cliente recupera timeline e versão ordenadas pelo snapshot HTTP, sem depender de mensagens perdidas.
- Arquitetura: domínio não depende de Spring/JPA; transporte WebSocket não contém regras de negócio.
