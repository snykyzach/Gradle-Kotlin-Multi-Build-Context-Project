package com.example.todoapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import javax.net.ssl.HttpsURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Hello Vulnerable World!"
        setContentView(textView)

        // --- VULNERABLE: Hardcoded secret ---
        val secretKey = "my_super_secret_key_12345"
        println("Secret: $secretKey")

        // --- VULNERABLE: Weak hashing algorithm ---
        val input = "SensitiveData"
        val hash = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        println("MD5 Hash: ${hash.joinToString("") { "%02x".format(it) }}")

        // --- VULNERABLE: Unsafe SSL connection (no cert validation) ---
        val url = URL("https://example.com")
        val connection = url.openConnection() as HttpsURLConnection
        connection.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
        connection.connect()
    }
}
