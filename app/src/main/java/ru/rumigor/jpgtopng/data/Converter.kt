package ru.rumigor.jpgtopng.data

import android.net.Uri
import io.reactivex.Single

interface Converter {
    fun convert(uri: Uri): Single<Uri>
}