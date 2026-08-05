# 005 - Restauração da verificação frontend

## Commit

`test: restore realtime frontend verification`

## Objetivo

Fazer o gate Angular executar um teste observável do cliente de snapshots em vez de falhar por ausência do target.

## Implementacao

- Target `@angular/build:unit-test` compatível com Angular 22.1.
- Vitest 4 e jsdom como dependências explícitas, com script `test:ci` não interativo.
- Teste do `OperationsClient` que verifica método, URL e versão retornada pelo snapshot persistido.
- `AGENTS.md` atualizado com o comando que a automação deve executar.

## Rastreabilidade ADR

Decisao local sem ADR novo: o incremento restaura apenas o seam de verificação existente e não muda arquitetura ou comportamento de produção.

## Verificacao

- `npm --prefix frontend test -- --watch=false` antes do incremento — falhou com `Unknown argument: watch`.
- `npm --prefix frontend test` antes do incremento — falhou com `Cannot determine project or target for command`.
- `PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend ci` — aprovado.
- `PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run test:ci` — aprovado: 1 teste.
- `PATH=/opt/homebrew/opt/node@24/bin:$PATH npm --prefix frontend run build` — aprovado.
- `JAVA_HOME=/opt/homebrew/opt/openjdk@21 ./mvnw verify` — aprovado: 7 testes.
- `./scripts/verify-traceability.sh` e `git diff --check` — aprovados.

## Alternativas e trade-offs

Não foi criado teste de componente: o cliente HTTP é o seam menor que prova a releitura autoritativa após sinais STOMP.

## Proximo passo

Manter `test:ci` no gate obrigatório de mudanças futuras do frontend.
