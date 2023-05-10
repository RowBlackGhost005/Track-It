package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Usuario
import mx.edu.itson.trackit.databinding.ActivityMainBinding
import mx.edu.itson.trackit.databinding.ActivityMyAccountBinding
import mx.edu.itson.trackit.databinding.ActivityMyAccountSettingsBinding

class MyAccountSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMyAccountSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account_settings)

        binding = ActivityMyAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        if (user != null && user.isEmailVerified) {
            fetchUserData()
        }


        binding.ibMyAccountMyTrackings.setOnClickListener{
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        binding.ibMyAccountMyAccount.setOnClickListener{
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }

        binding.ibMyAccountSettingsBack.setOnClickListener{
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }


    }

    private fun fetchUserData() {
        val firestoreDatabase = Firebase.firestore
        val docRef = firestoreDatabase.collection("users").document(auth.uid.toString())

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    loadUserData(document.toObject(Usuario::class.java))
                    Log.d("DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "get failed with ", exception)
            }
    }

    private fun loadUserData(user:Usuario?){
        binding.tvMyAccountSettingsUserName.text = user!!.username
        binding.etMyAccountSettingsFullName.setText(user!!.name)
        binding.etMyAccountSettingsEmail.setText(user!!.email)
    }
}