package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class VerifyAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_account)

        var btnCreateAccount: Button = findViewById(R.id.btnVerifyAccount_createAccount)

        btnCreateAccount.setOnClickListener(){
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }
    }
}