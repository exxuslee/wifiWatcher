package org.exxuslee.wifiwatcher.data.remote

import org.exxuslee.wifiwatcher.extension.dotenv
import org.exxuslee.wifiwatcher.extension.getRequiredEnv
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class TelegramService {

    private val token = getRequiredEnv("TELEGRAM_TOKEN", dotenv)
    private val client = OkHttpTelegramClient(token)


    fun sendMessage(text: String, chatId: String) {
        if (text.isBlank()) return
        val msg = SendMessage.builder()
            .chatId(chatId)
            .text(text)
            .disableWebPagePreview(true)
            .build()
        try {
            val response = client.execute(msg)
            println("✅ sendMessage: ${response.messageId}")
        } catch (e: Exception) {
            println("❌ sendMessage: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

}