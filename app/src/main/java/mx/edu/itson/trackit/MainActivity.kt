package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
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

        if (user != null && user.isEmailVerified) {
            authSuccess()
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
                LogIn(mEmail,mPassword)
            }
            }
        }



        var btnRegister: Button = findViewById(R.id.btnMain_register)

        btnRegister.setOnClickListener(){
            var intent = Intent(this , CreateAccount::class.java)
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

    private fun LogIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Log.d("AUTH","signInWithEmail:success")
                    Log.d("AUTH", auth.uid.toString())

                    if (auth.currentUser?.isEmailVerified() == true){
                        authSuccess()
                    }else{
                        Toast.makeText(baseContext,"Porfavor valide su correo electrónico.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,VerifyAccount::class.java)
                        intent.putExtra("userEmail" , binding.etMainUser.text.toString())
                        this.startActivity(intent)
                    }

                }else{
                    Log.w("TAG","signInWithEmail:failure",task.exception)
                    Toast.makeText(baseContext,"Error al iniciar sesión.",
                        Toast.LENGTH_SHORT).show()
                    //UpdateUI(null)
                }
            }


    }

    private fun authSuccess(){
        val intent = Intent(this,MainPage::class.java)
        this.startActivity(intent)
    }


}