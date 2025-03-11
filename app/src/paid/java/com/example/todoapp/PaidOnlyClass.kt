package com.example.todoapp

import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader
import java.io.InputStreamReader

class PaidOnlyClass {

    fun insecureQuery(userInput: String, db: SQLiteDatabase) {
        // VULNERABLE: SQL Injection via string concatenation
        val query = "SELECT * FROM users WHERE name = '$userInput'"
        db.rawQuery(query, null)
    }

    fun insecureCommandInjection(userInput: String) {
        // VULNERABLE: Command Injection
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
}
