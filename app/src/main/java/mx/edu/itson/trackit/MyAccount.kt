package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MyAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        val btnabout_us: Button = findViewById(R.id.btnMyAccount_about)

        btnabout_us.setOnClickListener{
            var intent: Intent = Intent(this , about_us::class.java)
            startActivity(intent)
        }

    }
}