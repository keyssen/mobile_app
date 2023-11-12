//package com.nodj.hardwareStore.db.database
//
//import android.graphics.BitmapFactory
//import com.nodj.hardwareStore.R
//import java.io.File
//import java.io.FileInputStream
//import java.io.IOException
//import kotlin.io.path.Path
//import kotlin.io.path.absolutePathString
//
//
//class Image {
//    fun getImage(id: Int): ByteArray? {
//        val filePath = "C:\\i5.jpg"
//        val imgFile = File(filePath)
//        val converters = Converters()
//        if (imgFile.exists()) {
//            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            return converters.fromBitmap(myBitmap)
//        }
//        val myBitmap = BitmapFactory.decodeResource(null, id)
//        return converters.fromBitmap(myBitmap)
//    }
//
//    fun imageFileToByteArray(): ByteArray {
//        val fullPath = Path("i5.jpg").absolutePathString()
//        val file = File(fullPath)
//
//        try {
//            val inputStream = FileInputStream(file)
//            val byteArray = ByteArray(file.length().toInt())
//
//            // Read the bytes of the image into the byte array
//            inputStream.read(byteArray)
//
//            // Close the input stream
//            inputStream.close()
//
//            return byteArray
//        } catch (e: IOException) {
//            // Handle the exception, e.g., log an error
//            e.printStackTrace()
//        }
//
//        // Return an empty byte array in case of an error
//        return byteArrayOf()
//    }
//}