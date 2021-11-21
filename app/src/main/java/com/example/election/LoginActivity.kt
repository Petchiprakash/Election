package com.example.election

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val username = tiUser.text.toString().trim()
            val password = tiPass.text.toString().trim()
            when {
                username.isEmpty() -> {
                    tiUser.error = "Enter Username"
                    tiUser.requestFocus()
                }
                password.isEmpty() -> {
                    tiPass.error = "Enter Password"
                    tiPass.requestFocus()
                }
                else -> {
                    val name = tiUser.text.toString()
                    val pass = tiPass.text.toString()
                    if (name == "admin" && pass == "admin") {
                        val intent = Intent(this, ResultActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Wrong Username or Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


        }

    }
}
