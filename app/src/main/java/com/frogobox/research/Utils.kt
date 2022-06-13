package com.frogobox.research

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/*
 * Created by faisalamir on 13/06/22
 * Research
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * -----------------------------------------
 * Copyright (C) 2022 Frogobox Media Inc.      
 * All rights reserved
 *
 */

object Utils {

    fun sendSStoWhatsapp(context: Context, view: View, title: String, messageOnWA: String) {
        // which view you want to pass that view as parameter
        val file: File? = saveBitMap(context, view)

        if (file != null) {
            Log.i("TAG", "Drawing saved to the gallery!")
            shareFileOnWhatsapp(context, title, messageOnWA, file)
        } else {
            Log.i("TAG", "Oops! Image could not be saved.")
        }
    }

    private fun shareFileOnWhatsapp(
        context: Context,
        title: String,
        messageOnWA: String,
        imageFile: File
    ) {
        val uri = FileProvider.getUriForFile(context, context.packageName, imageFile)
        try {
            val intentShareFile = Intent(Intent.ACTION_SEND)
            intentShareFile.type = "*/*"
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
            intentShareFile.putExtra(
                Intent.EXTRA_SUBJECT,
                "Share"
            )
            intentShareFile.putExtra(Intent.EXTRA_TEXT, messageOnWA)
            context.startActivity(Intent.createChooser(intentShareFile, title))
        } catch (e: java.lang.Exception) {
            Log.e("SHARE", "Exception occured sharing file", e)
        }
    }

    private fun saveBitMap(context: Context, drawView: View): File? {
        val imagePath = File(context.filesDir, "external_files")
        Log.d("TAG", "imagePath: $imagePath")
        if (!imagePath.exists()) {
            val isDirectoryCreated: Boolean = imagePath.mkdirs()
            if (!isDirectoryCreated) Log.i("ATG", "Can't create directory to save the image")
            return null
        }
        val imageFile: String =
            imagePath.path + File.separator + System.currentTimeMillis().toString() + ".jpg"
        Log.d("TAG", "imageFile: $imageFile")
        val pictureFile = File(imageFile)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            getBitmapFromView(drawView).compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }
        scanGallery(context, pictureFile.absolutePath)
        return pictureFile
    }

    //create bitmap from view and returns it
    private fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    // used for scanning gallery
    private fun scanGallery(context: Context, path: String) {
        try {
            MediaScannerConnection.scanFile(
                context, arrayOf(path), null
            ) { _, _ -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}