package ru.rumigor.jpgtopng.data

import android.content.Context
import android.net.Uri
import io.reactivex.Single

class ConverterImp (private val context: Context) : Converter {
    override fun convert(uri: Uri): Single<Uri> = ConverterSingle(context, uri)

}