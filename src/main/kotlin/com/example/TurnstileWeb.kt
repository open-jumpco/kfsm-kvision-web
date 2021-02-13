package com.example

import com.example.kfsm.Turnstile
import com.example.kfsm.TurnstileEvent
import com.example.kfsm.TurnstileFSM
import com.example.kfsm.TurnstileState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.state.ObservableValue
import kotlin.text.Typography.nbsp

abstract class TurnstileWeb(locked: Boolean) : VPanel(classes = setOf("card")), Turnstile {

    protected val fsm: TurnstileFSM
    private var _locked: Boolean
    override val locked: Boolean get() = _locked

    val messageText = ObservableValue<String>("$nbsp")
    val errorText = ObservableValue<String>("$nbsp")
    val state: ObservableValue<TurnstileState>

    init {
        _locked = locked
        fsm = TurnstileFSM(this)
        state = ObservableValue<TurnstileState>(fsm.currentState())
        updateState(fsm.currentState())
    }

    suspend fun coin() {
        GlobalScope.launch { fsm.coin() }
    }

    suspend fun pass() {
        GlobalScope.launch { fsm.pass() }
    }

    private suspend fun updateMessage(text: String) {
        messageText.value = text
        if (text.trim().isNotEmpty()) {
            GlobalScope.launch {
                delay(2000)
                messageText.value = "$nbsp"
            }
        }
    }

    private suspend fun updateError(error: String) {
        errorText.value = error
        if (error.trim().isNotEmpty()) {
            GlobalScope.launch {
                delay(5000)
                errorText.value = "$nbsp"
            }
        }
    }

    override fun updateState(state: TurnstileState) {
        this.state.value = state
    }

    override suspend fun lock() {
        require(!locked) { "Expected to be unlocked" }
        _locked = true
        console.log("lock")
        updateMessage("$nbsp")
        updateError("$nbsp")
    }

    override suspend fun unlock() {
        require(locked) { "Expected to be locked" }
        _locked = false
        console.log("unlock")
        updateMessage("$nbsp")
        updateError("$nbsp")
    }

    override suspend fun returnCoin() {
        updateMessage("Return Coin")
        console.log("return coin")
    }

    override suspend fun timeout() {
        updateError("Timeout")
        console.log("timeout");
        _locked = true
    }

    override suspend fun alarm() {
        updateError("Alarm")
        console.log("alarm")
    }
}