package ru.tehuchet.teh_uchet

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tehuchet.teh_uchet.data.User
import ru.tehuchet.teh_uchet.data.toNewUser
import ru.tehuchet.teh_uchet.data.withId
import ru.tehuchet.teh_uchet.net.BASE_URL
import ru.tehuchet.teh_uchet.net.IdResponseData
import ru.tehuchet.teh_uchet.net.ServerApi
import ru.tehuchet.teh_uchet.net.SuccessResponse
import kotlin.random.Random

class EditDataActivity : AppCompatActivity() {
    companion object {
        val PARAM_USER = "PARAM_USER"
        val PARAM_MODE = "PARAM_MODE"

        enum class EditMode {
            EDIT,
            ADD_NEW
        }

        fun start(ctx: Context, user: User? = null, mode: EditMode = EditMode.ADD_NEW) {
            val intent = Intent(ctx, EditDataActivity::class.java)
            intent.putExtra(PARAM_USER, user)
            intent.putExtra(PARAM_MODE, mode)
            ctx.startActivity(intent)
        }
    }

    private lateinit var fio: EditText
    private lateinit var phone: EditText
    private lateinit var mobile: EditText
    private lateinit var address: EditText
    private lateinit var kross: EditText
    private lateinit var magistral: EditText
    private lateinit var raspred: EditText
    private lateinit var adslPort: EditText
    private lateinit var boxes: EditText
    private lateinit var lat: EditText
    private lateinit var lng: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private lateinit var database: AppPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_data)

        fio = findViewById(R.id.fio)
        phone = findViewById(R.id.phone)
        mobile = findViewById(R.id.mobile)
        address = findViewById(R.id.address)
        kross = findViewById(R.id.kross)
        magistral = findViewById(R.id.magistral)
        raspred = findViewById(R.id.raspred)
        adslPort = findViewById(R.id.adslPort)
        boxes = findViewById(R.id.boxes)
        lat = findViewById(R.id.lat)
        lng = findViewById(R.id.lng)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        val mode = intent.getSerializableExtra(PARAM_MODE) as EditMode

        var newUserId = Random.nextLong()
        intent.getSerializableExtra(PARAM_USER)?.let { user ->
            user as User
            fio.setText(user.fio)
            phone.setText(user.phone)
            mobile.setText(user.mobile)
            address.setText(user.address)
            kross.setText(user.kross.toString())
            magistral.setText(user.magistral.toString())
            raspred.setText(user.raspred.toString())
            adslPort.setText(user.adsl.toString())
            val stringBuilder = StringBuilder()
            for (box in user.boxes) {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append("; ")
                }
                stringBuilder.append(box)
            }
            boxes.setText("$stringBuilder")
            lat.setText(user.lat.toString())
            lng.setText(user.long.toString())

            deleteButton.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Удалить абонента?")
                    .setMessage("Данные будут удалены без возможности восстановления")
                    .setPositiveButton("Да", object: DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                            val serverApi = Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(ServerApi::class.java)

                            serverApi.deleteUser(user.id).enqueue(object : Callback<SuccessResponse> {
                                override fun onResponse(
                                    p0: Call<SuccessResponse>,
                                    p1: Response<SuccessResponse>
                                ) {
                                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                                    if (p1.isSuccessful) {
                                        val intent =
                                            Intent(this@EditDataActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this@EditDataActivity, "Не удалось удалить абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(p0: Call<SuccessResponse>, p1: Throwable) {
                                    findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                                    Toast.makeText(this@EditDataActivity, "Не удалось удалить абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                                }

                            })
                        }
                    })
                    .setNegativeButton("Нет", null).show()

            }
            newUserId = user.id
        }


        deleteButton.isVisible = mode == EditMode.EDIT
        saveButton.setOnClickListener {
            val newUser = User(
                newUserId,
                fio.text.toString(),
                address.text.toString(),
                phone.text.toString(),
                mobile.text.toString(),
                kross.text.toString().toIntOrNull() ?: 1,
                magistral.text.toString().toIntOrNull() ?: 1,
                raspred.text.toString().toIntOrNull() ?: 1,
                adslPort.text.toString().toIntOrNull() ?: 1,
                boxes.text.toString().replace(" ", "").split(";"),
                lat.text.toString().toDoubleOrNull() ?: 0.0,
                lng.text.toString().toDoubleOrNull() ?: 0.0
            )

            val serverApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServerApi::class.java)


            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            if (mode == EditMode.ADD_NEW) {
                serverApi.createUser(newUser.toNewUser()).enqueue(object: Callback<IdResponseData> {
                    override fun onResponse(
                        p0: Call<IdResponseData>,
                        p1: Response<IdResponseData>
                    ) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        if (p1.isSuccessful) {
                            UserDataActivity.start(this@EditDataActivity, newUser.withId(p1.body()!!.id.toLong()))
                            finish()
                        } else {
                            Toast.makeText(this@EditDataActivity, "Не удалось создать абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(p0: Call<IdResponseData>, p1: Throwable) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        Toast.makeText(this@EditDataActivity, "Не удалось создать абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                serverApi.updateUser(newUserId, newUser).enqueue(object: Callback<SuccessResponse> {
                    override fun onResponse(
                        p0: Call<SuccessResponse>,
                        p1: Response<SuccessResponse>
                    ) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        if (p1.isSuccessful) {
                            UserDataActivity.start(this@EditDataActivity, newUser)
                            finish()
                        } else {
                            Toast.makeText(this@EditDataActivity, "Не удалось отредактировать абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(p0: Call<SuccessResponse>, p1: Throwable) {
                        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                        Toast.makeText(this@EditDataActivity, "Не удалось отредактировать абонента, проверьте соединение с сервером", Toast.LENGTH_LONG).show()
                    }

                })

            }
        }
    }
}