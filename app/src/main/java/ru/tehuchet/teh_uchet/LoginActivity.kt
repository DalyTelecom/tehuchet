package ru.tehuchet.teh_uchet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import ru.tehuchet.teh_uchet.ext.performLogin

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginInput: EditText = findViewById(R.id.login)
        val passwordInput:EditText = findViewById(R.id.password)
        val btn: Button = findViewById(R.id.loginBtn)

        btn.setOnClickListener {
            performLogin(
                login = loginInput.text.toString(),
                password = passwordInput.text.toString(),
                onSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}