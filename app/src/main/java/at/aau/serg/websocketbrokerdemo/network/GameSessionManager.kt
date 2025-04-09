package at.aau.serg.websocketbrokerdemo.network

import MyStomp
import at.aau.serg.websocketbrokerdemo.core.actions.Action
import org.hildan.krossbow.stomp.StompClient

/**
 * Handelt die Netzwerkkommunikation mit dem Spielserver.
 */

class GameSessionManager(private val stompClient: MyStomp) {
    /**
     * Sendet eine Spielaktion an den Server
     * @param action Die auszuführende Aktion
     * @throws IOException Bei Netzwerkfehlern
     */
    fun sendGameAction(action: Action) {
        TODO()
    }
}