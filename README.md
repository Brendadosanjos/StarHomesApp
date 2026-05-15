# 🏠 Star Homes

> Assistente inteligente para busca de imóveis em Londres — Aplicativo Android nativo desenvolvido em Kotlin com Jetpack Compose.

---

## 📱 Sobre o Projeto

O **Star Homes** é um aplicativo Android desenvolvido como projeto acadêmico da **Universidade de Fortaleza (UNIFOR)**, curso de Análise e Desenvolvimento de Sistemas.

O app foi criado originalmente na disciplina **N700 — Desenvolvimento para Plataformas Móveis** e posteriormente evoluído na disciplina **Transformação Digital**, onde passou por um ciclo completo de melhoria técnica — incluindo auditoria e correção de acessibilidade, refatoração de código, otimizações de performance, configuração de pipeline CI/CD e monitoramento pós-lançamento.

A solução centraliza a busca por imóveis para locação, permitindo que o usuário configure seu perfil, visualize bairros recomendados, faça tour virtual dos imóveis, agende visitas e gerencie seus favoritos — tudo em uma interface dark mode moderna baseada em Material Design 3.

---

## ♿ Acessibilidade

Como parte da disciplina **Transformação Digital**, o app passou por duas rodadas de auditoria de acessibilidade com o **Accessibility Scanner** do Google, com foco em usuários com deficiência visual que utilizam o **TalkBack** (leitor de tela do Android). Foram realizadas **20+ correções em todas as 13 telas** do aplicativo.

---

## 🔧 Transformação Digital

Além da acessibilidade, a disciplina **Transformação Digital** conduziu um ciclo completo de evolução do produto:

| Etapa | O que foi feito |
|---|---|
| **Diagnóstico** | Mapeamento de 13 telas, auditoria de acessibilidade, análise estática com Android Lint, avaliação de usabilidade com Heurísticas de Nielsen, definição de 5 KPIs |
| **Refatorações** | UUID seguro para IDs, `toggleFavorite` simplificado, lógica de chat extraída para o ViewModel (SOLID - SRP), `SimpleDateFormat` → `DateTimeFormatter` (thread-safe) |
| **Performance** | `AsyncImage` com `placeholder` e `error` (elimina layout shift), `remember` para evitar recálculo em recomposições |
| **Usabilidade** | Validação de formulários com mensagens de erro por campo, validação por etapa na recuperação de senha, labels de extremidade no Slider de preço |
| **CI/CD** | Pipeline GitHub Actions com build, lint, 12 testes unitários e deploy automático via Firebase App Distribution |
| **Monitoramento** | Plano de monitoramento com Firebase Analytics|

---

## ✨ Funcionalidades

- 🔐 Login, cadastro com validação e recuperação de senha por etapas
- 👤 Configuração de perfil com tipo, faixa de preço e prioridades
- 📍 **GPS** — localização real do usuário via sensor nativo
- 📷 **Câmera** — foto de perfil via câmera nativa ou galeria
- 🏘️ Busca e listagem de bairros recomendados
- 🏠 Detalhes de imóveis com fotos, informações e mapa
- 🎥 Tour virtual dos cômodos com planta baixa interativa
- 📅 Agendamento de visitas com calendário e horários disponíveis
- ❤️ Favoritar imóveis com persistência local
- 💾 **Armazenamento local** — dados persistem após fechar o app
- 📊 Relatório de preferências com gráfico de rosca
- ✏️ Edição de perfil com troca de foto

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Uso |
|---|---|
| Kotlin | Linguagem principal |
| Jetpack Compose | UI declarativa |
| Material Design 3 | Sistema de design |
| ViewModel + StateFlow | Gerenciamento de estado |
| FusedLocationProviderClient | Sensor GPS |
| FileProvider + TakePicture | Sensor de câmera |
| SharedPreferences | Armazenamento local |
| NotificationCompat | Notificações locais |
| Coil | Carregamento de imagens com placeholder |
| Kotlinx Coroutines | Programação assíncrona |
| java.time (DateTimeFormatter) | Formatação de datas thread-safe |
| Firebase Crashlytics | Monitoramento de crashes |
| Firebase Analytics | Eventos de uso e retenção |
| Firebase App Distribution | Distribuição de builds para testadores |
| GitHub Actions | Pipeline CI/CD automatizado |

---

## 📋 Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17+
- Android SDK API 24 (Android 7.0) ou superior
- Dispositivo ou emulador com API 24+

---

## 🚀 Como Rodar o Projeto

1. **Clone o repositório**
```bash
git clone https://github.com/Brendadosanjos/StarHomesApp
```

2. **Abra no Android Studio**
```
File → Open → selecione a pasta StarHomes
```

3. **Aguarde o Gradle sincronizar**

4. **Rode o app**
```
Selecione um dispositivo ou emulador → clique em ▶ Run
```

> ⚠️ Para testar GPS e câmera, recomenda-se usar um **dispositivo físico**.

---

## 🤖 CI/CD

O projeto possui pipeline automatizado no **GitHub Actions** (`.github/workflows/ci.yml`):

- **Todo push/PR:** Android Lint + 12 testes unitários + build do APK
- **Push na `main`:** deploy automático via Firebase App Distribution para testadores

Para configurar o deploy, adicione os seguintes secrets no repositório (`Settings → Secrets and variables → Actions`):

| Secret | Descrição |
|---|---|
| `FIREBASE_APP_ID` | ID do app no Firebase Console |
| `FIREBASE_TOKEN` | Token gerado via `firebase login:ci` |

---

## 📂 Estrutura do Projeto

```
app/src/main/java/com/starhomes/app/
├── MainActivity.kt              # Ponto de entrada e navegação
├── AppViewModel.kt              # Estado global e lógica de negócio
├── data/
│   ├── Models.kt                # Modelos de dados
│   └── MockData.kt              # Dados simulados
├── ui/
│   ├── Theme.kt                 # Tema, cores e tipografia (WCAG AA)
│   └── components/
│       └── AppComponents.kt     # Header, Footer, campos reutilizáveis
│   └── screens/
│       ├── LoginScreen.kt
│       ├── SignUpScreen.kt       # Com validação de formulário
│       ├── ForgotPasswordScreen.kt # Com validação por etapa
│       ├── ProfileSetupScreen.kt
│       ├── ChatScreen.kt
│       ├── SearchResultsScreen.kt
│       ├── NeighborhoodDetailsScreen.kt
│       ├── PropertyDetailsScreen.kt
│       ├── VirtualTourScreen.kt
│       ├── ScheduleVisitScreen.kt
│       ├── AppointmentsScreen.kt
│       ├── FavoritesScreen.kt
│       ├── EditProfileScreen.kt
│       └── PreferencesReportScreen.kt
├── location/
│   └── LocationManager.kt       # Gerenciamento do sensor GPS
├── storage/
│   └── StorageManager.kt        # Persistência via SharedPreferences
└── notification/
    └── NotificationHelper.kt    # Notificações locais
```

---

## 📸 Permissões Utilizadas

| Permissão | Finalidade |
|---|---|
| `ACCESS_FINE_LOCATION` | Localização precisa via GPS |
| `ACCESS_COARSE_LOCATION` | Localização aproximada (fallback) |
| `CAMERA` | Foto de perfil via câmera nativa |
| `POST_NOTIFICATIONS` | Notificações locais (Android 13+) |
| `READ_EXTERNAL_STORAGE` | Galeria (Android 12 ou inferior) |
| `INTERNET` | Carregamento de imagens |

---

## 🎓 Informações Acadêmicas

- **Instituição:** Universidade de Fortaleza — UNIFOR
- **Curso:** Análise e Desenvolvimento de Sistemas
- **Disciplina de origem:** N700 — Desenvolvimento para Plataformas Móveis
- **Disciplina de evolução:** Transformação Digital
- **Período:** 2026.1