package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MyAccountSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account_settings)

        val btnTrackings: ImageButton = findViewById(R.id.ibMyAccount_myTrackings)
        val btnMyAccount: ImageButton = findViewById(R.id.ibMyAccount_myAccount)
        val btnBack: ImageButton = findViewById(R.id.ibMyAccountSettings_back)


        btnTrackings.setOnClickListener{
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        btnMyAccount.setOnClickListener{
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener{
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }


    }
}