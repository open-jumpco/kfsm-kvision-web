package com.example

import io.kvision.Application
import io.kvision.core.*
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.tr
import io.kvision.module
import io.kvision.navbar.nav
import io.kvision.navbar.navLink
import io.kvision.navbar.navbar
import io.kvision.panel.flexPanel
import io.kvision.panel.root
import io.kvision.require
import io.kvision.startApplication

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
