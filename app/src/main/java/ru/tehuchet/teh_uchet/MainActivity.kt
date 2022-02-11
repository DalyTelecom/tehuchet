package ru.tehuchet.teh_uchet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tehuchet.teh_uchet.data.User
import ru.tehuchet.teh_uchet.net.BASE_URL
import ru.tehuchet.teh_uchet.net.ServerApi
import ru.tehuchet.teh_uchet.net.UsersResponseData

class MainActivity : AppCompatActivity() {
    private lateinit var name: AutoCompleteTextView
    private lateinit var number: AutoCompleteTextView
    private lateinit var address: AutoCompleteTextView
    private lateinit var data: Button
    private lateinit var addUser: Button
    private lateinit var users: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        name = findViewById(R.id.lastName)
        number = findViewById(R.id.number)
        address = findViewById(R.id.address)
        data = findViewById(R.id.data)
        addUser = findViewById(R.id.addUser)
    }

    private fun initData() {
        name.setText("")
        number.setText("")
        address.setText("")

        val namesFromDb = users.map {
            it.fio
        }

        name.setAdapter(
            ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_dropdown_item_1line, namesFromDb
            )
        )

        val numbersFromDb = users.map {
            it.phone
        }

        number.setAdapter(
            ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_dropdown_item_1line, numbersFromDb
            )
        )

        val addrsFromDb = users.map {
            it.address
        }

        address.setAdapter(
            ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_dropdown_item_1line, addrsFromDb
            )
        )

        name.addTextChangedListener { text ->
            users.firstOrNull {
                it.fio == text.toString()
            }?.let { user ->
                number.setText(user.phone)
                address.setText(user.address)
            }
        }

        addUser.setOnClickListener {
            EditDataActivity.start(this)
        }

        data.setOnClickListener {
            val familiaText: String = name.text.toString()
            val phone: String = number.text.toString()
            val addr: String = address.text.toString()

            var user: User? = null

            if (familiaText.isNotEmpty()) {
                user = users.firstOrNull {
                    it.fio == familiaText
                }
            } else if (phone.isNotEmpty()) {
                user = users.firstOrNull {
                    it.phone == phone
                }
            } else if (addr.isNotEmpty()) {
                user = users.firstOrNull {
                    it.address == addr
                }
            } else {
                Toast.makeText(this, "Введите данные для поиска", Toast.LENGTH_LONG).show()
            }

            user?.let {
                UserDataActivity.start(this, it)
                it
            } ?: run {
                Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val serverApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)

        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
        serverApi.getPeople().enqueue(object: Callback<UsersResponseData> {
            override fun onResponse(p0: Call<UsersResponseData>, p1: Response<UsersResponseData>) {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                if (p1.isSuccessful) {
                    p1.body()?.let {
                        users = it.users
                        initData()
                        it
                    } ?: run {
                        Toast.makeText(this@MainActivity, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(p0: Call<UsersResponseData>, p1: Throwable) {
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                Toast.makeText(this@MainActivity, "Не удалось загрузить данные", Toast.LENGTH_SHORT).show()
            }

        })
    }
}