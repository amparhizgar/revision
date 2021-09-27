package com.amirhparhizgar.revision.model

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Test cases for [SpacedRepetition]
 *
 * note: this class is copied from [here](https://blog.mestwin.net/spaced-repetition-algorithm-implementation-in-kotlin)
 * and has been modified
 */
internal class SpacedRepetitionTest {

    private val spacedRepetition = SpacedRepetition

    @Test
    fun `should send single answer with quality 5 and check returned card`() {
        val card = getCard()

        val response = spacedRepetition.calculateRepetition(card, 5)

        assertEquals(1, response.repetitions)
        assertEquals(1, response.interval)
        assertEquals(2.6.toFloat(), response.easinessFactor)
        assertTrue(response.nextRepetitionMillis > System.currentTimeMillis())
    }

    @Test
    fun `should send single answer with quality 4 and check returned card`() {
        val card = getCard()

        val response = spacedRepetition.calculateRepetition(card, 4)

        assertEquals(1, response.repetitions)
        assertEquals(1, response.interval)
        assertEquals(2.5.toFloat(), response.easinessFactor)
        assertTrue(response.nextRepetitionMillis > System.currentTimeMillis())
    }

    @Test
    fun `should send single answer with quality 3 and check returned card`() {
        val card = getCard()

        val response = spacedRepetition.calculateRepetition(card, 3)

        assertEquals(1, response.repetitions)
        assertEquals(1, response.interval)
        assertEquals(2.36.toFloat(), response.easinessFactor)
        assertTrue(response.nextRepetitionMillis > System.currentTimeMillis())
    }

    @Test
    fun `should send single answer with quality 2 and check returned card`() {
        val card = getCard()

        val response = spacedRepetition.calculateRepetition(card, 2)

        assertEquals(0, response.repetitions)
        assertEquals(1, response.interval)
        assertEquals(2.18.toFloat(), response.easinessFactor)
        assertTrue(response.nextRepetitionMillis > System.currentTimeMillis())
    }

    @Test
    fun `should send single answer with quality 1 and check returned card`() {
        val card = getCard()

        val response = spacedRepetition.calculateRepetition(card, 1)

        assertEquals(0, response.repetitions)
        assertEquals(1, response.interval)
        assertEquals(1.96.toFloat(), response.easinessFactor)
        assertTrue(response.nextRepetitionMillis > System.currentTimeMillis())
    }

    @Test
    fun `should simulate successful cards repetition session and expect no more cards to repeat today`() {
        val deck = getDeckWithSixCards()

        val cardsAfterRepetition = deck.map { flashCard ->
            val repetition1 = spacedRepetition.calculateRepetition(flashCard, 1)
            val repetition2 = spacedRepetition.calculateRepetition(repetition1, 2)
            val repetition3 = spacedRepetition.calculateRepetition(repetition2, 3)
            val repetition4 = spacedRepetition.calculateRepetition(repetition3, 4)
            spacedRepetition.calculateRepetition(repetition4, 5)
        }

        val cardToRepeatToday =
            cardsAfterRepetition.filter { it.nextRepetitionMillis < System.currentTimeMillis() }
        assertTrue(cardToRepeatToday.isEmpty())
    }

    @Test
    fun `should throw an exception if the user's quality of repetition response is invalid`() {
        val flashCard = getCard(repetitionDate = System.currentTimeMillis() - dayInMs)

        assertThrows(IllegalArgumentException::class.java) {
            spacedRepetition.calculateRepetition(flashCard, 6)
        }
        assertThrows(IllegalArgumentException::class.java) {
            spacedRepetition.calculateRepetition(flashCard, -1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            spacedRepetition.calculateRepetition(flashCard, Int.MAX_VALUE)
        }
        assertThrows(IllegalArgumentException::class.java) {
            spacedRepetition.calculateRepetition(flashCard, Int.MIN_VALUE)
        }
    }

    private fun getCard(
        title: String = "title",
        repetitionDate: Long = System.currentTimeMillis()
    ) = Task(
        id = -1,
        name = title,
        project = "project",
        nextRepetitionMillis = repetitionDate
    )


    private fun getDeckWithSixCards() = listOf(
        getCard(),
        getCard(repetitionDate = System.currentTimeMillis() - dayInMs),
        getCard(repetitionDate = System.currentTimeMillis() - dayInMs * 2),
        getCard(repetitionDate = System.currentTimeMillis() + dayInMs * 2),
        getCard(repetitionDate = System.currentTimeMillis() + dayInMs),
        getCard(repetitionDate = System.currentTimeMillis() - dayInMs * 3)
    )

    private val dayInMs = 24 * 60 * 60 * 1000
}