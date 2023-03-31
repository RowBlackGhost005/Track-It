package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MyAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        val btnabout_us: Button = findViewById(R.id.btnMyAccount_about)
        val btnHistory: Button = findViewById(R.id.btnMyAccount_history)
        val btnFaq: Button = findViewById(R.id.btnMyAccount_faq)
        val btnCarrier: Button = findViewById(R.id.btnMyAccount_carrier)
        val btnTrackings: ImageButton = findViewById(R.id.ibMyAccount_myTrackings)
        val btnSettings: Button = findViewById(R.id.btnMyAccount_settings)

        btnSettings.setOnClickListener{
            var intent: Intent = Intent(this , settings::class.java)
            startActivity(intent)
        }

        btnTrackings.setOnClickListener{
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        btnCarrier.setOnClickListener{
            var intent: Intent = Intent(this , shipper::class.java)
            intent.putExtra("tipo","nacional")
            startActivity(intent)
        }

        btnabout_us.setOnClickListener{
            var intent: Intent = Intent(this , about_us::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener{
            var intent: Intent = Intent(this , history::class.java)
            startActivity(intent)
        }

        btnFaq.setOnClickListener{
            var intent: Intent = Intent(this , Frequently_asked::class.java)
            startActivity(intent)
        }

    }
}