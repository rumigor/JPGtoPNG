package ru.rumigor.jpgtopng.mvp


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import io.reactivex.schedulers.Schedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.rumigor.jpgtopng.data.Converter
import ru.rumigor.jpgtopng.data.ConverterFactory
import ru.rumigor.jpgtopng.data.ConverterImp
import ru.rumigor.jpgtopng.databinding.ActivityMainBinding
import ru.rumigor.jpgtopng.scheduler.SchedulersFactory

const val GALLERY_REQUEST = 1

class MainActivity : MvpAppCompatActivity(), MainView {
    private val ui: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var uri: Uri
    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            converter = ConverterFactory(applicationContext).create(),
            SchedulersFactory.create()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ui.root)
        loadImage()
    }

    override fun loadImage() {
        ui.btnLoadImg.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK);
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }
    }

    override fun transformImage(uri: Uri) {
        ui.btnTransform.setOnClickListener {
            presenter.transform(uri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap = null
        uri = data?.data!!
        ui.image.setImageURI(uri)
        transformImage(uri)
    }
}