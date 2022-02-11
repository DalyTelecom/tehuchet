package ru.tehuchet.teh_uchet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ru.tehuchet.teh_uchet.data.User
import java.util.*

class UserDataActivity : AppCompatActivity() {
    companion object {
        const val PARAM_USER = "PARAM_USER"

        fun start(ctx: Context, user: User) {
            val intent = Intent(ctx, UserDataActivity::class.java)
            intent.putExtra(PARAM_USER, user)
            ctx.startActivity(intent)
        }
    }

    private lateinit var nameTV: TextView
    private lateinit var phoneTV: TextView
    private lateinit var mobileTV: TextView
    private lateinit var addrTV: TextView
    private lateinit var krossTV: TextView
    private lateinit var magistralTV: TextView
    private lateinit var raspredTV: TextView
    private lateinit var adslTV: TextView
    private lateinit var boxesTV: TextView
    private lateinit var editButton: Button
    private lateinit var viewOnMap: Button

    private var passedUser: User? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        passedUser = intent.getSerializableExtra(PARAM_USER) as User

        setContentView(R.layout.activity_user_data)
        nameTV = findViewById(R.id.name)
        phoneTV = findViewById(R.id.phone)
        mobileTV = findViewById(R.id.mobile)
        addrTV = findViewById(R.id.address)
        krossTV = findViewById(R.id.kross)
        magistralTV = findViewById(R.id.magistral)
        raspredTV = findViewById(R.id.raspredelenie)
        adslTV = findViewById(R.id.adslPort)
        boxesTV = findViewById(R.id.boxes)
        viewOnMap = findViewById(R.id.view)
        editButton = findViewById(R.id.edit)
    }

    override fun onResume() {
        super.onResume()

        passedUser?.let { user ->
            nameTV.text = user.fio
            phoneTV.text = user.phone
            mobileTV.text = user.mobile
            addrTV.text = "Адрес: ${user.address}"
            krossTV.text = "Кросс: ${user.kross}"
            magistralTV.text = "Магистраль: ${user.magistral}"
            raspredTV.text = "Распределение: ${user.raspred}"
            adslTV.text = "ADSL: ${user.adsl}"

            val stringBuilder = StringBuilder()
            for (box in user.boxes) {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append("; ")
                }
                stringBuilder.append(box)
            }
            boxesTV.text = "Ящик: ${stringBuilder}"

            viewOnMap.setOnClickListener {
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", user.lat, user.long)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }

            editButton.setOnClickListener {
                EditDataActivity.start(this, user, EditDataActivity.Companion.EditMode.EDIT)
                finish()
            }
            user
        } ?: run {
            finish()
        }

    }
}