package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnRegister: Button = findViewById(R.id.btnMain_register)

        btnRegister.setOnClickListener(){
            var intent: Intent = Intent(this , CreateAccount::class.java)
            startActivity(intent)
        }

        var btnLogin: Button = findViewById(R.id.btnMain_login)

        btnLogin.setOnClickListener(){
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }
    }
}