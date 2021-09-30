package ru.rumigor.jpgtopng.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.MainThreadDisposable

class ConverterSingle(
    private val context: Context,
    private val uri: Uri
) : Single<Uri>() {
    override fun subscribeActual(observer: SingleObserver<in Uri>) {
        val listener = ConverterListener(context, uri, observer)
        observer.onSubscribe(listener)
        listener.convert()
    }

    class ConverterListener(
        private val context: Context,
        private val uri: Uri,
        private val observer: SingleObserver<in Uri>
    ) : MainThreadDisposable(), Runnable {

        private val converterTemporary = File.createTempFile("fileToTransform", null)
        private val converterTask by lazy {
            Executors
                .newSingleThreadExecutor()
                .submit(this)
        }

        fun convert() {
            converterTask
        }

        override fun onDispose() {
            converterTask
                ?.takeIf { !isDisposed }
                ?.takeIf { task -> !task.isDone }
                ?.takeIf { task -> !task.isCancelled }
                ?.cancel(true)
                ?.also{clearConverterTemporary()}
        }

        override fun run() {
            try {
                BufferedOutputStream(FileOutputStream(converterTemporary)).use { fos ->
                    MediaStore.Images.Media
                        .getBitmap(context.contentResolver, uri)
                        .compress(Bitmap.CompressFormat.PNG, 10, fos)

                }
                converterTask
                    ?.takeIf { !isDisposed }
                    ?.takeIf { task -> !task.isDone }
                    ?.takeIf { task -> task.isCancelled }
                    ?.let { observer.onSuccess(uri) }
            } catch (e: Throwable) {
                observer.onError(e)
            } finally {
                clearConverterTemporary()
            }
        }

        private fun clearConverterTemporary() {
            converterTemporary
                .takeIf(File::exists)
                ?.let(File::delete)
        }

    }

}