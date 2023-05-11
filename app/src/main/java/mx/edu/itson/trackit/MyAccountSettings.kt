package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        }else{
            var intent: Intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
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

        binding.ibMyAccountSettingsLogout.setOnClickListener(){
            auth.signOut()
            Toast.makeText(baseContext, "Cerrando sesión. . .",
                Toast.LENGTH_SHORT).show()
            var intent: Intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountSettingsEditAccount.setOnClickListener{
            binding.btnMyAccountSettingsConfirmChanges.visibility = View.VISIBLE
            binding.btnMyAccountSettingsDeleteAccount.visibility = View.VISIBLE
            binding.btnMyAccountSettingsEditAccount.visibility = View.GONE
            binding.etMyAccountSettingsUserName.visibility = View.VISIBLE
            binding.etMyAccountSettingsUserName.isEnabled = true
            binding.etMyAccountSettingsFullName.isEnabled = true
            binding.etMyAccountSettingsEmail.isEnabled = true
        }

        binding.btnMyAccountSettingsConfirmChanges.setOnClickListener{
            binding.btnMyAccountSettingsConfirmChanges.visibility = View.GONE
            binding.btnMyAccountSettingsDeleteAccount.visibility = View.GONE
            binding.etMyAccountSettingsUserName.visibility = View.GONE
            binding.btnMyAccountSettingsEditAccount.visibility = View.VISIBLE
            binding.etMyAccountSettingsUserName.isEnabled = false
            binding.etMyAccountSettingsFullName.isEnabled = false
            binding.etMyAccountSettingsEmail.isEnabled = false

            var newUserName = binding.etMyAccountSettingsUserName.text.toString()
            var newFullName = binding.etMyAccountSettingsFullName.text.toString()
            var newEmail = binding.etMyAccountSettingsEmail.text.toString()

            updateUserData(auth.uid.toString(), newUserName, newFullName, newEmail)

        }

        binding.btnMyAccountSettingsDeleteAccount.setOnClickListener{
            val builder = AlertDialog.Builder(this@MyAccountSettings)
            builder.setMessage("Esta seguro de borrar su cuenta?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->

                    val firestoreDatabase =  Firebase.firestore
                    firestoreDatabase.collection("users").document(auth.uid.toString())
                        .delete()
                        .addOnSuccessListener { Log.d("DB", "Usuario eliminado correctamente!") }
                        .addOnFailureListener { e -> Log.w("DB", "Error deleting document", e) }

                    auth.currentUser?.delete()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("AUTH", "User account deleted.")
                            }
                        }

                    auth.signOut()
                    //Return to Login
                    val intent= Intent(this, MainActivity::class.java)
                    this.startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }


    }

    private fun updateUserData(userUid:String, newUserName:String, newFullName:String, newEmail:String){
        val firestoreDatabase =  Firebase.firestore

        val washingtonRef = firestoreDatabase.collection("users").document(userUid)

        washingtonRef
            .update("username", newUserName, "name", newFullName, "email" , newEmail)
            .addOnSuccessListener { Log.d("DB", "Datos del usuario actualizados!") ; Toast.makeText(baseContext, "Datos actualizados", Toast.LENGTH_SHORT).show() ; fetchUserData()}
            .addOnFailureListener { e -> Log.w("DB", "Error actualizando el usuario", e) ; Toast.makeText(baseContext, "Error al actualizar los datos", Toast.LENGTH_SHORT).show() }
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
        binding.etMyAccountSettingsUserName.setText(user!!.username)
        binding.etMyAccountSettingsFullName.setText(user!!.name)
        binding.etMyAccountSettingsEmail.setText(user!!.email)

        binding.btnMyAccountSettingsConfirmChanges.visibility = View.GONE
        binding.btnMyAccountSettingsDeleteAccount.visibility = View.GONE
        binding.etMyAccountSettingsUserName.visibility = View.GONE
        binding.etMyAccountSettingsUserName.isEnabled = false
        binding.etMyAccountSettingsFullName.isEnabled = false
        binding.etMyAccountSettingsEmail.isEnabled = false
    }
}