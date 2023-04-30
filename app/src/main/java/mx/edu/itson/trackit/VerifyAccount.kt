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

        var btnCreateAccount: Button = findViewById(R.id.btnVerifyAccount_createAccount)

        btnCreateAccount.setOnClickListener(){

            var email= getIntent().getStringExtra("email")
            var password = getIntent().getStringExtra("password")

            CreateAccount(email.toString(),password.toString())
        }
    }

    private fun CreateAccount(email:String,password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    //val user = auth.currentUser
                    //updateUI(user)
                    reaload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun reaload(){
        val intent= Intent(this,MainPage::class.java)
        this.startActivity(intent)
    }
}