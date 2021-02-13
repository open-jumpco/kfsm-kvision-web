package com.example

import com.example.kfsm.TurnstileEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.html.*
import pl.treksoft.kvision.i18n.tr
import pl.treksoft.kvision.panel.flexPanel
import kotlin.text.Typography.nbsp

class TurnstileView(
    private val number: Int,
    locked: Boolean,
    override var parent: Container?,
    override var visible: Boolean = true
) : TurnstileWeb(locked) {

    private val passButton =
        Button(tr("pass"), icon = "fas fa-door-open", classes = setOf("btn", "btn-primary", "m-2"))
    private val coinButton =
        Button(tr("coin"), icon = "fab fa-cc-visa", classes = setOf("btn", "btn-secondary", "m-2"))
    private val text = Span(content = "$nbsp", classes = setOf("m-2"))
    private val error = Span(content = "$nbsp", classes = setOf("m-2"))
    private val deleteButton =
        Button(tr("delete"), icon = "fas fa-trash", classes = setOf("btn", "btn-warn", "m-2"))
    private val stateSpan = Span("")
    private val stateSubTitle = H6(classes = setOf("m-2")).add(Span(tr("state"))).add(Span("$nbsp")).add(stateSpan)

    init {
        passButton.onClick { GlobalScope.launch { pass() } }
        coinButton.onClick { GlobalScope.launch { coin() } }
        deleteButton.onClick { Model.delete(number) }
        error.color = Color.name(Col.RED)
        text.color = Color.name(Col.BLUE)
        messageText.subscribe { text.content = it }
        errorText.subscribe { error.content = it }
        state.subscribe {
            stateSpan.content = it.toString()
            checkButtons()
        }
        div(classes = setOf("card-body", "p-0")) {
            h5(classes = setOf("m-2")).add(Span(tr("turnstile"))).add(Span(":$nbsp")).add(Span(number.toString()))
            add(stateSubTitle)
            p {
                add(text)
                add(error)
            }
            flexPanel(
                direction = FlexDirection.ROW,
                alignContent = AlignContent.SPACEBETWEEN,
                classes = setOf("card-footer", "align-middle", "m-0")
            ) {
                add(passButton)
                add(coinButton)
                add(deleteButton)
            }
        }
    }

    private fun checkButtons() {
        passButton.disabled = !fsm.allowed(TurnstileEvent.PASS)
        coinButton.disabled = !fsm.allowed(TurnstileEvent.COIN)
    }
}