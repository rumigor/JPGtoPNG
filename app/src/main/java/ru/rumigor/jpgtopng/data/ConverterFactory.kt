package ru.rumigor.jpgtopng.data

import android.content.Context

class ConverterFactory (private val context:Context){
    fun create () : Converter = ConverterImp(context)
}