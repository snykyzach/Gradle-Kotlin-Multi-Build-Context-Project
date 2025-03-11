package com.example.todoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.EditText
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.security.MessageDigest

class PaidOnlyClass {

    // --- SQL Injection ---
    fun insecureQuery(context: Context, db: SQLiteDatabase) {
        val userInput = getUserInput() // Simulated untrusted input
        val query = "SELECT * FROM users WHERE name = '$userInput'"
        db.rawQuery(query, null)
    }

    fun getUserInput(): String {
        return "attacker_input"
    }

    // --- Command Injection ---
    fun insecureCommandInjection() {
        val scanner = java.util.Scanner(System.`in`)
        print("Enter command args: ")
        val userInput = scanner.nextLine() // Simulated untrusted input for command
        val command = "ls $userInput"
        try {
            val process = Runtime.getRuntime().exec(command)
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

    // --- Weak Hashing ---
    fun weakHashing(context: Context): String {
        val editText = EditText(context)
        val data = editText.text.toString() // Simulated user input
        val md = MessageDigest.getInstance("MD5") // Weak hash
        val hashBytes = md.digest(data.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    // --- Hardcoded Secrets ---
    fun hardcodedSecrets() {
        val apiKey = "sk_live_51H8dK7EXAMPLEAPIKEYSHOULDNOTBEHERE"
        val password = "P@ssw0rd123!"
        println("API Key: $apiKey")
        println("Password: $password")
    }

    // --- Insecure SSL Connection ---
    fun insecureSSLConnection() {
        try {
            val url = URL("https://example.com")
            val conn = url.openConnection() as HttpsURLConnection
            conn.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
            conn.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Insecure Trust Manager ---
    fun insecureTrustManager() {
        val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(
            object : javax.net.ssl.X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
            }
        )
        val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    }

    // --- Path Traversal ---
    fun pathTraversal(context: Context) {
        val editText = EditText(context)
        val userInput = editText.text.toString() // Simulated untrusted input
        val filePath = "/data/data/com.example.todoapp/files/$userInput"
        val file = java.io.File(filePath)
        if (file.exists()) {
            println("File contents: ${file.readText()}")
        }
    }

    // --- Dynamic Class Loading (Reflection) ---
    fun dynamicClassLoading() {
        val scanner = java.util.Scanner(System.`in`)
        print("Enter class name to load: ")
        val userInput = scanner.nextLine() // Simulated untrusted input
        try {
            val clazz = Class.forName(userInput) // Dangerous reflection
            println("Class loaded: $clazz")
        } catch (e: ClassNotFoundException) {
            println("Class not found")
        }
    }

    // --- Insecure Broadcast ---
    fun insecureBroadcast(context: Context) {
        val editText = EditText(context)
        val message = editText.text.toString() // Simulated untrusted message
        val intent = android.content.Intent()
        intent.action = "com.example.todoapp.INSECURE_ACTION"
        intent.putExtra("message", message)
        context.sendBroadcast(intent)
    }
}
