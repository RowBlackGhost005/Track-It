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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.databinding.ActivityCreateAccountBinding
import mx.edu.itson.trackit.databinding.ActivityMainBinding

class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCreateAccountBinding
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnCreateAccountContinue.setOnClickListener{
            if(validateData()){
                createAccount(binding.etCreateAccountEmail.text.toString() , binding.etCreateAccountPassword.text.toString())
            }
        }
    }

    private fun validateData(): Boolean{

        //User Data
        var userFullname = binding.etCreateAccountFullName.text.toString()
        var userUsername = binding.etCreateAccountUsername.text.toString()
        var userEmail = binding.etCreateAccountEmail.text.toString()
        var userPassword = binding.etCreateAccountPassword.text.toString()
        var userConfirmedPassword = binding.etCreateAccountConfirmPassword.text.toString()

        //Validar datos

        if(userFullname.isEmpty()){
            Toast.makeText(baseContext,"Ingrese su nombre completo", Toast.LENGTH_SHORT).show()
            return false
        }

        if(userUsername.isEmpty()){
            Toast.makeText(baseContext,"Ingrese un nombre de usuario", Toast.LENGTH_SHORT).show()
            return false
        }

        if(userPassword.length < 3){
            Toast.makeText(baseContext,"La contraseña debe tener mas de 3 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }

        if(userEmail.isEmpty()){
            Toast.makeText(baseContext,"Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            Toast.makeText(baseContext,"Correo electrónico inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        if(userPassword != userConfirmedPassword){
            Toast.makeText(baseContext,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun createAccount(email:String,password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                createAccountDatabase()
                                Log.d("AUTH", "Verification email sent.")
                            }
                        }
                    Log.d("AUTH", "createUserWithEmail: success")

                    this.user = user

                    val intent= Intent(this,VerifyAccount::class.java)
                    intent.putExtra("userEmail" , binding.etCreateAccountEmail.text.toString())
                    intent.putExtra("username" , binding.etCreateAccountUsername.text.toString())
                    this.startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ERR", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error al crear la cuenta.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun createAccountDatabase(){
        val firestoreDatabase =  Firebase.firestore

        val user = hashMapOf(
            "uid" to user!!.uid,
            "name" to binding.etCreateAccountFullName.text.toString(),
            "username" to binding.etCreateAccountUsername.text.toString() ,
            "email" to user!!.email,
            "profilePic" to "default",
            "activateCode" to generateActivationCode(),
            "parcels" to ArrayList<String>(),
            "isActivated" to false

        )

        firestoreDatabase.collection("users").document(this.user!!.uid)
            .set(user)
            .addOnSuccessListener { Log.d("Firestore", "Usuario creado correctamente!") }
            .addOnFailureListener { e -> Log.w("Firestore", "Error writing document", e) }
    }

    private fun generateActivationCode():Int{
        var activationCode = ""
        val randNumbers = {(0..9).random()}

        for(number: Int in 1..4){
            val number = randNumbers().toString()
            activationCode += number
        }

        return activationCode.toInt()
    }


}