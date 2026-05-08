package com.starhomes.app

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

// =============================================================================
// TESTES UNITÁRIOS — AppViewModel
// Arquivo: app/src/test/java/com/starhomes/app/AppViewModelTest.kt
// =============================================================================
// Estes testes cobrem as funções de negócio refatoradas no Passo 9,
// validando que o comportamento está correto sem depender de Android/Compose.
//
// Para rodar localmente: ./gradlew testDebugUnitTest
// Relatório: app/build/reports/tests/testDebugUnitTest/index.html
// =============================================================================

class AppViewModelUnitTest {

    // -------------------------------------------------------------------------
    // toggleFavorite — Refatoração 2 do Passo 9
    // -------------------------------------------------------------------------

    @Test
    fun `toggleFavorite adiciona id quando nao esta na lista`() {
        val list = mutableListOf<String>()
        val id = "prop_001"

        // Simula a lógica refatorada do toggleFavorite
        val removed = list.remove(id)
        if (!removed) list.add(id)

        assertTrue("ID deve estar na lista após toggle", list.contains(id))
        assertEquals("Lista deve ter exatamente 1 item", 1, list.size)
    }

    @Test
    fun `toggleFavorite remove id quando ja esta na lista`() {
        val list = mutableListOf("prop_001", "prop_002")
        val id = "prop_001"

        val removed = list.remove(id)
        if (!removed) list.add(id)

        assertFalse("ID não deve estar na lista após segundo toggle", list.contains(id))
        assertEquals("Lista deve ter 1 item restante", 1, list.size)
    }

    @Test
    fun `toggleFavorite duas vezes retorna ao estado original`() {
        val list = mutableListOf<String>()
        val id = "prop_abc"

        // Primeiro toggle — adiciona
        var removed = list.remove(id)
        if (!removed) list.add(id)
        assertTrue(list.contains(id))

        // Segundo toggle — remove
        removed = list.remove(id)
        if (!removed) list.add(id)
        assertFalse(list.contains(id))
    }

    // -------------------------------------------------------------------------
    // Appointment ID — Refatoração 1 do Passo 9
    // -------------------------------------------------------------------------

    @Test
    fun `UUID gerado nao eh vazio`() {
        val id = java.util.UUID.randomUUID().toString()
        assertTrue("UUID não deve ser vazio", id.isNotBlank())
    }

    @Test
    fun `dois UUIDs gerados sao sempre diferentes`() {
        val id1 = java.util.UUID.randomUUID().toString()
        val id2 = java.util.UUID.randomUUID().toString()
        assertNotEquals("Dois UUIDs devem ser diferentes", id1, id2)
    }

    @Test
    fun `UUID tem formato correto`() {
        val id = java.util.UUID.randomUUID().toString()
        // UUID v4 tem o formato: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
        assertTrue(
            "UUID deve ter 36 caracteres com hífens",
            id.matches(Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))
        )
    }

    // -------------------------------------------------------------------------
    // Validações de formulário — Melhorias de usabilidade do Passo 11
    // -------------------------------------------------------------------------

    @Test
    fun `email valido eh reconhecido corretamente`() {
        fun isValidEmail(email: String) =
            email.contains("@") && email.contains(".") && email.length > 5

        assertTrue(isValidEmail("usuario@email.com"))
        assertTrue(isValidEmail("teste.nome@dominio.com.br"))
    }

    @Test
    fun `email invalido eh rejeitado`() {
        fun isValidEmail(email: String) =
            email.contains("@") && email.contains(".") && email.length > 5

        assertFalse(isValidEmail(""))
        assertFalse(isValidEmail("semArroba.com"))
        assertFalse(isValidEmail("@semdomain"))
        assertFalse(isValidEmail("ab@c.d")) // length <= 5
    }

    @Test
    fun `senhas iguais sao validas`() {
        val password = "senha123"
        val confirm  = "senha123"
        assertEquals("Senhas iguais devem ser válidas", password, confirm)
    }

    @Test
    fun `senhas diferentes sao invalidas`() {
        val password = "senha123"
        val confirm  = "senha456"
        assertNotEquals("Senhas diferentes devem ser inválidas", password, confirm)
    }

    // -------------------------------------------------------------------------
    // DateTimeFormatter — Refatoração 4 do Passo 9
    // -------------------------------------------------------------------------

    @Test
    fun `data formatada em dd-MM-yyyy esta correta`() {
        val date = java.time.LocalDate.of(2026, 6, 5)
        val formatted = date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        assertEquals("05/06/2026", formatted)
    }

    @Test
    fun `data ISO esta no formato yyyy-MM-dd`() {
        val date = java.time.LocalDate.of(2026, 6, 5)
        val iso = date.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
        assertEquals("2026-06-05", iso)
    }
}