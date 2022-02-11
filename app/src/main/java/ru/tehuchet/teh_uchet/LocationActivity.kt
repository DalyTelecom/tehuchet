package ru.tehuchet.teh_uchet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.net.Uri
import java.util.*


class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        val lat = 53.9422
        val long = 36.6997

        val seeOnMap: Button = findViewById(R.id.seeOnMap)
        seeOnMap.setOnClickListener {
            val uri: String =
                java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", lat, long)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }
}