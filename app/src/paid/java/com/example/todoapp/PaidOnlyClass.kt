package com.example.todoapp

import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.security.MessageDigest

class PaidOnlyClass {

    fun insecureQuery(userInput: String, db: SQLiteDatabase) {
        // SQL Injection via string concatenation
        val query = "SELECT * FROM users WHERE name = '$userInput'"
        db.rawQuery(query, null)
    }

    fun insecureCommandInjection(userInput: String) {
        // Command Injection
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

    fun weakHashing(data: String): String {
        // Weak cryptographic hashing (MD5)
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(data.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun hardcodedSecrets() {
        // Hardcoded API key
        val apiKey = "sk_live_51H8dK7EXAMPLEAPIKEYSHOULDNOTBEHERE"
        println("API Key: $apiKey")

        // Hardcoded password
        val password = "P@ssw0rd123!"
        println("Password: $password")
    }

    fun insecureSSLConnection() {
        // Insecure SSL connection example
        try {
            val url = URL("https://example.com")
            val conn = url.openConnection() as HttpsURLConnection
            conn.hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier() // Could be overridden for MITM
            conn.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
