package com.starhomes.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.starhomes.app.data.Appointment
import com.starhomes.app.data.MockData
import com.starhomes.app.data.Screen
import com.starhomes.app.data.User
import com.starhomes.app.storage.StorageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

// =============================================================================
// REFATORAÇÃO 1 — UUID seguro para IDs de agendamento
// -----------------------------------------------------------------------------
// ANTES:
//   id = "app_${System.currentTimeMillis()}"
//
// PROBLEMA: System.currentTimeMillis() pode gerar IDs idênticos se dois
// agendamentos forem criados no mesmo milissegundo (ex.: testes rápidos,
// double-tap acidental). Isso causa colisões silenciosas na lista e no storage.
//
// DEPOIS:
//   id = UUID.randomUUID().toString()
//
// UUID v4 gera 122 bits aleatórios — probabilidade de colisão desprezível.
// É o padrão da indústria para IDs de entidades locais sem backend.
// =============================================================================

// =============================================================================
// REFATORAÇÃO 2 — toggleFavorite simplificado (Clean Code)
// -----------------------------------------------------------------------------
// ANTES:
//   if (favoritePropertyIds.contains(propertyId)) {
//       favoritePropertyIds.remove(propertyId)
//   } else {
//       favoritePropertyIds.add(propertyId)
//   }
//
// PROBLEMA: lógica if/else verbosa para uma operação de toggle simples.
// Viola o princípio "Don't Repeat Yourself" — a condição e a operação
// estão acopladas desnecessariamente.
//
// DEPOIS:
//   val removed = favoritePropertyIds.remove(propertyId)
//   if (!removed) favoritePropertyIds.add(propertyId)
//
// remove() retorna true se o elemento existia e foi removido, false caso
// contrário — eliminamos a chamada extra a contains().
// =============================================================================

// =============================================================================
// REFATORAÇÃO 3 — Lógica de chat extraída do Composable para o ViewModel
// -----------------------------------------------------------------------------
// ANTES: handleSend() com delay e navigateTo ficavam dentro do ChatScreen
// (Composable), misturando lógica de negócio com lógica de apresentação.
//
// PROBLEMA: viola o princípio da Responsabilidade Única (SOLID - SRP).
// Composables devem apenas observar estado e despachar eventos — não
// controlar fluxos assíncronos ou decidir para qual tela navegar.
// Também torna o comportamento do chat impossível de testar unitariamente.
//
// DEPOIS: ChatMessage, mensagens e handleSend() vivem no ViewModel.
// O Composable apenas observa chatMessages e chatNavigation via StateFlow,
// e chama vm.handleChatSend(input) ao enviar. Navegação é disparada pelo
// ViewModel via chatNavigation e consumida uma única vez pelo Composable.
// =============================================================================

data class ChatMessage(val from: String, val text: String)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = StorageManager(application)

    // ── Estado do usuário ────────────────────────────────────────────────────
    var user = mutableStateOf(MockData.USER)
        private set

    // ── Navegação entre telas ────────────────────────────────────────────────
    var selectedNeighborhoodId = mutableStateOf<String?>(null)
        private set

    var selectedPropertyId = mutableStateOf<String?>(null)
        private set

    // ── Favoritos ────────────────────────────────────────────────────────────
    val favoritePropertyIds = mutableStateListOf<String>().also { list ->
        list.addAll(storage.loadFavorites())
    }

    // ── Agendamentos ─────────────────────────────────────────────────────────
    val appointments = mutableStateListOf<Appointment>().also { list ->
        list.addAll(storage.loadAppointments())
    }

    // ── Chat (REFATORAÇÃO 3) ─────────────────────────────────────────────────
    // Mensagens do chat expostas como lista observável ao Composable.
    val chatMessages = mutableStateListOf(
        ChatMessage("bot", "Olá! Sou o assistente da Star Homes. Posso coletar algumas informações adicionais sobre você?")
    )

    // StateFlow para navegação do chat — emite a tela destino uma única vez
    // e volta a null, evitando renavegação em recomposições.
    private val _chatNavigation = MutableStateFlow<Screen?>(null)
    val chatNavigation: StateFlow<Screen?> = _chatNavigation

    // Lógica de envio de mensagem movida para cá (REFATORAÇÃO 3 — SRP).
    // O Composable só chama esta função e observa chatMessages/chatNavigation.
    fun handleChatSend(input: String) {
        if (input.isBlank()) return
        chatMessages.add(ChatMessage("user", input.trim()))
        viewModelScope.launch {
            delay(1000)
            chatMessages.add(
                ChatMessage(
                    "bot",
                    "Entendido! Baseado em suas preferências, estou buscando os melhores bairros. Um momento..."
                )
            )
            delay(2000)
            _chatNavigation.value = Screen.SEARCH_RESULTS
        }
    }

    // Chamado pelo Composable após consumir a navegação — reseta o estado
    // para evitar renavegação em recomposições futuras.
    fun onChatNavigationConsumed() {
        _chatNavigation.value = null
    }

    // ── Funções de usuário ───────────────────────────────────────────────────
    fun updateUser(newUser: User) {
        user.value = newUser
    }

    fun selectNeighborhood(id: String) {
        selectedNeighborhoodId.value = id
    }

    fun selectProperty(id: String) {
        selectedPropertyId.value = id
    }

    // ── Favoritos ────────────────────────────────────────────────────────────
    fun toggleFavorite(propertyId: String) {
        // REFATORAÇÃO 2: remove() retorna false se o elemento não existia,
        // eliminando a chamada extra a contains() do código original.
        val removed = favoritePropertyIds.remove(propertyId)
        if (!removed) favoritePropertyIds.add(propertyId)
        storage.saveFavorites(favoritePropertyIds.toSet())
    }

    // ── Agendamentos ─────────────────────────────────────────────────────────
    fun addAppointment(propertyId: String, type: String, date: String, time: String) {
        val appointment = Appointment(
            // REFATORAÇÃO 1: UUID v4 no lugar de System.currentTimeMillis()
            // — sem risco de colisão de IDs em operações rápidas.
            id = UUID.randomUUID().toString(),
            propertyId = propertyId,
            type = type,
            date = date,
            time = time
        )
        appointments.add(appointment)
        appointments.sortBy { it.date }
        storage.saveAppointments(appointments.toList())
    }

    fun cancelAppointment(id: String) {
        appointments.removeAll { it.id == id }
        storage.saveAppointments(appointments.toList())
    }
}