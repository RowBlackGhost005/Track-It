package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.databinding.ActivityCreateAccountBinding
import mx.edu.itson.trackit.databinding.ActivityMainBinding

class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var btnContinue: Button = findViewById(R.id.btnCreateAccount_continue)



        btnContinue.setOnClickListener(){

            var addImage: ImageView = findViewById(R.id.ivCreateAccount_addImage)
            var fullname: EditText = findViewById(R.id.etCreateAccount_fullName)
            var username: EditText = findViewById(R.id.etCreateAccount_username)
            var email: EditText = findViewById(R.id.etCreateAccount_email)
            var password: EditText = findViewById(R.id.etCreateAccount_password)
            var passwordConfirm: EditText = findViewById(R.id.etCreateAccount_confirmPassword)

            if(password.text.toString() == passwordConfirm.text.toString()){
                val intent= Intent(this,VerifyAccount::class.java)
                intent.putExtra("addImage",1)
                intent.putExtra("fullname",fullname.text.toString())
                intent.putExtra("username",username.text.toString())
                intent.putExtra("email",email.text.toString())
                intent.putExtra("password",password.text.toString())

                this.startActivity(intent)
            }else{
                Toast.makeText(baseContext,"Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }



}