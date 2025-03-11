package com.example.todoapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection
import java.net.URL

class TabletOnlyClass {

    fun insecureSharedPreferences(context: Context) {
        // VULNERABLE: Insecure data storage (M2)
        val sharedPref: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token", "hardcoded_token_123456") // Sensitive data stored insecurely
        editor.apply()
    }

    fun insecureWebView(context: Context) {
        // VULNERABLE: Improper platform usage (M1) - JavaScript enabled without validation
        val webView = WebView(context)
        webView.settings.javaScriptEnabled = true // JavaScript enabled without checks
        webView.loadUrl("http://example.com") // Insecure URL, should be HTTPS
    }

    fun brokenAuth(context: Context, userInput: String) {
        // VULNERABLE: Broken authentication (A2) - user input used for critical check
        if (userInput == "admin") {
            println("Welcome Admin!") // Improper authentication mechanism
        }
    }

    fun insecureCommunication() {
        // VULNERABLE: Insecure communication (M3) - no SSL validation
        try {
            val url = URL("https://example.com")
            val conn = url.openConnection() as HttpsURLConnection
            conn.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
            conn.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun exposedDebugCommand(context: Context, cmd: String) {
        // VULNERABLE: Exposed debug functionality (M8) - Command Injection
        try {
            val process = Runtime.getRuntime().exec(cmd) // Dangerous command execution
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                println(line)
                line = reader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun weakHashing(data: String): String {
        // VULNERABLE: Weak hashing (MD5)
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(data.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
