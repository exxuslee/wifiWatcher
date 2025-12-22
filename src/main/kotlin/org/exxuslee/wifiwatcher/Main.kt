package org.exxuslee.wifiwatcher

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.exxuslee.wifiwatcher.data.local.Storage
import org.exxuslee.wifiwatcher.data.remote.TelegramService
import org.exxuslee.wifiwatcher.extension.dotenv
import org.exxuslee.wifiwatcher.extension.getRequiredEnv
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime

private val ssid = getRequiredEnv("SSID", dotenv)
private val adminChatId = getRequiredEnv("ADMIN_CHAT_ID", dotenv)
private val userChatId = getRequiredEnv("USER_CHAT_ID", dotenv)

fun main() {
    runBlocking {
        println("Welcome to WiFi Watcher!")
        val storage = Storage()
        val telegramService = TelegramService()
        var timeout: Long
        while (isActive) {
            try {
                val current = scanNetworks()
                if (current.isEmpty()) {
                    println("${LocalDateTime.now()} — Empty scan result, skipping...")
                    delay(60_000)
                    continue
                }
                println("${LocalDateTime.now()} — Scanned SSIDs: $current")
                val is1 = ssid in current
                val is2 = ssid !in storage.get()
                timeout = if (is1) 1200_000L else 60_000L
                if (is1 && is2) {
                    println("${LocalDateTime.now()} — $ssid detected! Sending Telegram...")
                    telegramService.sendMessage("💡 $current", adminChatId)
                }
                storage.putBatch(current)
            } catch (e: Exception) {
                println("Error: ${e.message}")
                timeout = 60_000L
            }
            delay(timeout)
        }
    }
}


private fun scanNetworks(): Set<String> {
    val process = ProcessBuilder("/sbin/iwlist", "wlan0", "scan")
        .redirectErrorStream(true)
        .start()
    val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
    process.waitFor()
    val regex = Regex("""ESSID:"([^"]+)"""")
    return regex.findAll(output).map { it.groupValues[1] }.toSet()
}