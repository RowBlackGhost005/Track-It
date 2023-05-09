package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.databinding.ActivityMainBinding
import mx.edu.itson.trackit.databinding.ActivityMainPageBinding

class MainPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var btnAddTrackingNumber: Button = findViewById(R.id.btnMainPage_addParcel)

        btnAddTrackingNumber.setOnClickListener(){
            var intent: Intent = Intent(this , AddTrackingNumber::class.java)
            startActivity(intent)
        }

        var myTrackings: ImageButton = findViewById(R.id.ibMainPage_myTrackings)

        myTrackings.setOnClickListener(){
            var intent: Intent = Intent(this , tracking_menu::class.java)
            startActivity(intent)
        }

        var myAccount: ImageButton = findViewById(R.id.ibMainPage_myAccount)

        myAccount.setOnClickListener(){
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }


    }


}