# 🏠 Star Homes

> Assistente inteligente para busca de imóveis em Londres — Aplicativo Android nativo desenvolvido em Kotlin com Jetpack Compose.

---

## 📱 Sobre o Projeto

O **Star Homes** é um aplicativo Android desenvolvido como projeto acadêmico para a disciplina **N700 — Desenvolvimento para Plataformas Móveis** da Universidade de Fortaleza (UNIFOR), curso de Análise e Desenvolvimento de Sistemas.

A solução centraliza a busca por imóveis para locação, permitindo que o usuário configure seu perfil, visualize bairros recomendados, faça tour virtual dos imóveis, agende visitas e gerencie seus favoritos — tudo em uma interface dark mode moderna baseada em Material Design 3.

---

## ✨ Funcionalidades

- 🔐 Login, cadastro e recuperação de senha
- 👤 Configuração de perfil com tipo, faixa de preço e prioridades
- 📍 **GPS** — localização real do usuário via sensor nativo
- 📷 **Câmera** — foto de perfil via câmera nativa ou galeria
- 🏘️ Busca e listagem de bairros recomendados
- 🏠 Detalhes de imóveis com fotos, informações e mapa
- 🎥 Tour virtual dos cômodos
- 📅 Agendamento de visitas com calendário e horários disponíveis
- ❤️ Favoritar imóveis
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
| ViewModel + State | Gerenciamento de estado |
| FusedLocationProviderClient | Sensor GPS |
| FileProvider + TakePicture | Sensor de câmera |
| SharedPreferences | Armazenamento local |
| NotificationCompat | Notificações locais |
| Coil | Carregamento de imagens |
| Kotlinx Coroutines | Programação assíncrona |

---

## 📋 Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 11+
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

## 📂 Estrutura do Projeto

```
app/src/main/java/com/starhomes/app/
├── MainActivity.kt          # Ponto de entrada e navegação
├── AppViewModel.kt          # Estado global da aplicação
├── data/
│   ├── Models.kt            # Modelos de dados
│   └── MockData.kt          # Dados simulados
├── ui/
│   ├── Theme.kt             # Tema, cores e tipografia
│   └── components/
│       └── AppComponents.kt # Header, Footer, campos reutilizáveis
│   └── screens/
│       ├── LoginScreen.kt
│       ├── SignUpScreen.kt
│       ├── ForgotPasswordScreen.kt
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
│   └── LocationManager.kt   # Gerenciamento do sensor GPS
├── storage/
│   └── StorageManager.kt    # Persistência via SharedPreferences
└── notification/
    └── NotificationHelper.kt # Notificações locais
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
- **Disciplina:** N700 — Desenvolvimento para Plataformas Móveis
- **Período:** 2026.1