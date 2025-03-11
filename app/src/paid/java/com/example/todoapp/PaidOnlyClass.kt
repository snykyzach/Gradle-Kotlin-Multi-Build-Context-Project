package com.example.todoapp

import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.security.MessageDigest

class PaidOnlyClass {

    fun insecureQuery(userInput: String, db: SQLiteDatabase) {
        val query = "SELECT * FROM users WHERE name = '$userInput'"
        db.rawQuery(query, null)
    }

    fun insecureCommandInjection(userInput: String) {
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
        val md = MessageDigest.getInstance("MD5")
        val hashBytes = md.digest(data.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun hardcodedSecrets() {
        val apiKey = "sk_live_51H8dK7EXAMPLEAPIKEYSHOULDNOTBEHERE"
        val password = "P@ssw0rd123!"
        println("API Key: $apiKey")
        println("Password: $password")
    }

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

    fun pathTraversal(userInput: String) {
        val filePath = "/data/data/com.example.todoapp/files/$userInput"
        val file = java.io.File(filePath)
        if (file.exists()) {
            println("File contents: ${file.readText()}")
        }
    }

    fun dynamicClassLoading(userInput: String) {
        try {
            val clazz = Class.forName(userInput)
            println("Class loaded: $clazz")
        } catch (e: ClassNotFoundException) {
            println("Class not found")
        }
    }

    fun insecureBroadcast(context: android.content.Context, message: String) {
        val intent = android.content.Intent()
        intent.action = "com.example.todoapp.INSECURE_ACTION"
        intent.putExtra("message", message)
        context.sendBroadcast(intent)
    }
}
