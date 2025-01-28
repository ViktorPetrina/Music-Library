package hr.vpetrina.music.handler

import android.content.Context
import android.util.Log
import hr.vpetrina.music.factory.createGetHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files

fun downloadImage(context: Context, url: String) : String? {
    val fileName = url.substring(url.lastIndexOf(File.separatorChar) + 1)
    val file: File = createFile(context, fileName)

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url)
        Files.copy(con.inputStream, file.toPath())
        return file.absolutePath

    } catch (e: Exception) {
        Log.e("IMAGES_HANDLER", e.message, e)
    }
    return null
}

fun createFile(context: Context, fileName: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, fileName)
    if (file.exists()) file.delete()
    return file
}
