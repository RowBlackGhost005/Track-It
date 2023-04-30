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
import mx.edu.itson.trackit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val user = Firebase.auth.currentUser
        if (user != null) {
            reaload()
        } else {

        }

        binding.btnMainLogin.setOnClickListener{
            val mEmail=binding.etMainUser.text.toString()
            val mPassword=binding.etMainPassword.text.toString()

            when{
                mEmail.isEmpty() || mPassword.isEmpty()->{
                    Toast.makeText(baseContext, "Mail o contraseña incorrecta.",
                        Toast.LENGTH_SHORT).show()
                }else ->{
                SignIn(mEmail,mPassword)
            }
            }
        }



        var btnRegister: Button = findViewById(R.id.btnMain_register)

        btnRegister.setOnClickListener(){
            var intent: Intent = Intent(this , CreateAccount::class.java)
            startActivity(intent)
        }

       /*
        var btnLogin: Button = findViewById(R.id.btnMain_login)

        btnLogin.setOnClickListener(){
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }
        */

    }

    private fun SignIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d("TAG","signInWithEmail:success")
                    //val user= auth.currentUser
                    //updateUI(user)
                    reaload()
                }else{
                    Log.w("TAG","signInWithEmail:failure",task.exception)
                    Toast.makeText(baseContext,"Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //UpdateUI(null)
                }
            }


    }

    private fun reaload(){
        val intent= Intent(this,MainPage::class.java)
        this.startActivity(intent)
    }
}