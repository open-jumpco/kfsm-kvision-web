package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.tr
import pl.treksoft.kvision.module
import pl.treksoft.kvision.navbar.nav
import pl.treksoft.kvision.navbar.navLink
import pl.treksoft.kvision.navbar.navbar
import pl.treksoft.kvision.panel.flexPanel
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication

class App : Application() {
    init {
        require("css/kvapp.css")
    }

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )

        root("kvapp") {
            navbar(tr("turnstile"), classes = setOf("w-100")) {
                nav {
                    navLink(tr("create"), classes = setOf("ml-auto", "btn", "btn-light")).onClick { Model.create() }
                }
            }
            turnstileList()
        }
    }

    private fun turnstileView(parent: Container, state: TurnstileComponentState): TurnstileView {
        return TurnstileView(state.number, state.locked, parent)
    }

    private fun Container.turnstileList() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.FLEXSTART, AlignItems.FLEXSTART,
            spacing = 5
        ) {
            Model.turnstiles.subscribe { list ->
                this.removeAll()
                list.turnstiles.forEach {
                    add(turnstileView(this, it))
                }
            }
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
