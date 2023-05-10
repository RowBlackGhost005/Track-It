package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.databinding.ActivityCreateAccountBinding
import mx.edu.itson.trackit.databinding.ActivityVerifyAccountBinding

class VerifyAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVerifyAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_account)

        binding = ActivityVerifyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var email = getIntent().getStringExtra("userEmail")
        var username = getIntent().getStringExtra("username")

        if (email != null) {
            var verifyText = binding.tvVerifyAccountVerifyEmailText.text
            binding.tvVerifyAccountVerifyEmailText.setText(verifyText.replaceFirst(Regex.fromLiteral("correo@mail.com") , email))
        }

        binding.tvVerifyAccountUsername.text = username


        binding.btnVerifyAccountCreateAccount.setOnClickListener(){
            reload()
        }
    }


    private fun reload(){
        val intent= Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}