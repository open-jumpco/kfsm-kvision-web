package com.example

import com.example.kfsm.TurnstileState
import kotlinx.serialization.Serializable
import io.kvision.state.ObservableValue

@Serializable
class TurnstileComponentState(val number: Int, var locked: Boolean, var state: TurnstileState? = null)

data class TurnstileList(
    val turnstiles: List<TurnstileComponentState>
)

object Model {
    var nextTurnstileNumber: Int = 1
    val turnstiles = ObservableValue(TurnstileList(mutableListOf()))

    fun create() {
        val list = turnstiles.value.turnstiles + TurnstileComponentState(nextTurnstileNumber++, true)
        turnstiles.value = turnstiles.value.copy(turnstiles = list)
    }

    fun delete(number: Int) {
        val list = turnstiles.value.turnstiles
        turnstiles.value = turnstiles.value.copy(turnstiles = list.filter { it.number != number })
    }
}