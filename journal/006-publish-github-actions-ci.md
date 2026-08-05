# 006 - Publicação do CI no GitHub Actions

## Commit

`ci: publish GitHub Actions workflow`

## Objetivo

Publicar o gate de integração contínua em `.github/workflows/ci.yml` sem reescrever o histórico já publicado.

## Implementacao

- Promove o workflow preservado na safety branch para o caminho ativo do GitHub Actions.
- Executa rastreabilidade, testes Maven, instalação reproduzível, teste e build Angular e validação do Compose.
- Inclui `test:ci`, restaurado no incremento anterior, como gate obrigatório do frontend.

## Rastreabilidade ADR

Decisao local sem ADR novo: trata-se da ativação do pipeline já projetado, sem mudança arquitetural.

## Verificacao

- Workflow e este Journal são os únicos arquivos do commit remoto atômico.
- Atualização de `main` feita sem force push.
- Execução do GitHub Actions verificada após a publicação.

## Alternativas e trade-offs

A publicação usa a GitHub App porque o token Git local não possui o escopo `workflow`.

## Proximo passo

Manter o workflow verde como gate dos próximos incrementos.
