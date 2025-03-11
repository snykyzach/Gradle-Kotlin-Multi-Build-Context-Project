package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.io.File
import java.lang.reflect.Method
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class PhoneOnlyClass {

    fun intentDataLeak(context: Context, sensitiveData: String) {
        // VULNERABLE: Exposing sensitive data via implicit intent (M3 / A6)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "Sensitive data: $sensitiveData") // Sensitive data in broadcast
        intent.type = "text/plain"
        context.sendBroadcast(intent) // ANY app can listen to this broadcast
    }

    fun exportFileToExternalStorage(context: Context, filename: String) {
        // VULNERABLE: Insecure data storage in external storage (M2)
        val file = File(context.getExternalFilesDir(null), filename) // World-readable file
        file.writeText("Sensitive data that should not be stored here")
    }

    fun insecureCrypto(data: String): ByteArray {
        // VULNERABLE: Using ECB mode (insecure) in AES (A6 / M5)
        val key = SecretKeySpec("MySecretKey12345".toByteArray(), "AES") // Weak hardcoded key
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // ECB mode is insecure
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(data.toByteArray())
    }

    fun dynamicReflection(userInput: String) {
        // VULNERABLE: Reflection based on user input (M8)
        try {
            val clazz = Class.forName(userInput)
            val method: Method = clazz.getDeclaredMethod("dangerousMethod")
            method.isAccessible = true
            method.invoke(clazz.newInstance()) // Potential RCE if userInput is controlled
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun contentProviderLeak(context: Context) {
        // VULNERABLE: Accessing and leaking content provider data insecurely (M4)
        val uri = Uri.parse("content://com.example.todoapp.provider/private_data")
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val secret = it.getString(0)
                Log.d("LeakedData", "Sensitive data: $secret") // Logging private data
            }
        }
    }

    fun exportDatabase(context: Context) {
        // VULNERABLE: Exporting DB to public storage (M2 / M4)
        val dbFile = context.getDatabasePath("app.db")
        val exportFile = File(context.getExternalFilesDir(null), "app_export.db")
        dbFile.copyTo(exportFile, overwrite = true) // DB copied to accessible location
    }
}
