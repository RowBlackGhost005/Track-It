package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        var btnContinue: Button = findViewById(R.id.btnCreateAccount_continue)

        btnContinue.setOnClickListener(){
            var intent: Intent = Intent(this , VerifyAccount::class.java)
            startActivity(intent)
        }
    }
}