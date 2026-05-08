# ✅ Checklist de Pré-Lançamento — Star Homes

> Preencher antes de qualquer deploy para o grupo de testadores (Firebase App Distribution)  
> ou submissão à Google Play Store.  
> **Todos os itens obrigatórios (🔴) devem estar marcados antes do deploy.**

---

## 🔴 Qualidade de Código

- [ ] Pipeline CI/CD passou sem erros (build + lint + testes)
- [ ] Zero erros de Lint críticos (`./gradlew lintDebug` sem falhas bloqueantes)
- [ ] Todos os testes unitários passando (`./gradlew testDebugUnitTest`)
- [ ] Nenhuma senha, token ou chave de API hardcoded no código-fonte
- [ ] Variáveis sensíveis configuradas via `local.properties` ou GitHub Secrets

---

## 🔴 Acessibilidade

- [ ] Auditoria com Accessibility Scanner executada em todas as telas principais
- [ ] Todos os botões e elementos interativos possuem `contentDescription`
- [ ] Contraste de cores verificado (mínimo WCAG AA — 4.5:1 para texto normal)
- [ ] Tamanhos de fonte mínimos respeitados (≥ 12sp)
- [ ] Navegação completa por TalkBack testada nos fluxos principais

---

## 🔴 Funcionalidade

- [ ] Fluxo de login testado (e-mail + senha)
- [ ] Fluxo de cadastro testado com validações funcionando
- [ ] Fluxo de recuperação de senha testado nas 3 etapas
- [ ] Busca e exibição de bairros funcionando
- [ ] Detalhes de imóvel carregando (imagens, preço, informações)
- [ ] Favoritar e desfavoritar imóvel funcionando e persistindo
- [ ] Agendamento de visita funcionando (data + hora + notificação)
- [ ] Cancelamento de agendamento com confirmação funcionando
- [ ] Tour virtual carregando imagens e planta baixa
- [ ] Chat com assistente navegando para resultados

---

## 🔴 Performance

- [ ] Tempo de carregamento da tela inicial < 3 segundos em rede 4G
- [ ] Sem ANR (Application Not Responding) nos fluxos principais
- [ ] Imagens carregando com placeholder visível (sem layout shift)
- [ ] Scroll das listas fluido (sem jank perceptível)

---

## 🟡 Dispositivos e Compatibilidade

- [ ] Testado em Android 8.0 (API 26) — mínimo suportado
- [ ] Testado em Android 14 (API 34) — versão atual
- [ ] Testado em tela pequena (5") e grande (6.7")
- [ ] Testado em modo retrato (portrait)
- [ ] Comportamento correto com fonte grande do sistema (Acessibilidade → Tamanho da fonte)
- [ ] Comportamento correto sem conexão com a internet

---

## 🟡 Configuração do App

- [ ] `versionCode` incrementado em relação à versão anterior
- [ ] `versionName` atualizado (ex.: 1.0.1)
- [ ] ProGuard/R8 configurado para build release (minificação ativa)
- [ ] Permissões do `AndroidManifest.xml` revisadas (somente as necessárias)
- [ ] `android:debuggable="false"` no build release

---

## 🟡 Firebase

- [ ] Firebase Crashlytics ativo e recebendo eventos de teste
- [ ] Firebase Analytics configurado com eventos principais mapeados
- [ ] Firebase App Distribution configurado com grupo de testadores atualizado
- [ ] `google-services.json` presente e atualizado (não commitar no repositório)

---

## 🟢 Documentação

- [ ] README do repositório atualizado com instruções de build
- [ ] Release notes redigidas para os testadores
- [ ] Commits do ciclo identificados com mensagens descritivas

---

**Responsável pelo deploy:** ___________________________  
**Data:** ___________________________  
**Versão:** ___________________________  
**Aprovado por:** ___________________________