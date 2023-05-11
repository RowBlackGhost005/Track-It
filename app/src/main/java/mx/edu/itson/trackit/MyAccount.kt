package mx.edu.itson.trackit

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import mx.edu.itson.trackit.databinding.ActivityMyAccountBinding
import mx.edu.itson.trackit.databinding.ActivityMyAccountSettingsBinding

class MyAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMyAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        if (user != null && user.isEmailVerified) {
            fetchUserData()
        } else {
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.ivMyAccountTrackPackage.setOnClickListener() {
            var intent: Intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        binding.ibMyAccountMyAccountSettings.setOnClickListener {
            var intent: Intent = Intent(this, MyAccountSettings::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountSettings.setOnClickListener {
            var intent: Intent = Intent(this, settings::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountTrackPackage.setOnClickListener {
            var intent: Intent = Intent(this, AddTrackingNumber::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountCarrier.setOnClickListener {
            var intent: Intent = Intent(this, shipper::class.java)
            intent.putExtra("tipo", "nacional")
            startActivity(intent)
        }

        binding.btnMyAccountAbout.setOnClickListener {
            var intent: Intent = Intent(this, about_us::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountHistory.setOnClickListener {
            var intent: Intent = Intent(this, history::class.java)
            startActivity(intent)
        }

        binding.btnMyAccountFaq.setOnClickListener {
            var intent: Intent = Intent(this, Frequently_asked::class.java)
            startActivity(intent)
        }

    }

    private fun fetchUserData() {
        val firestoreDatabase = Firebase.firestore
        val docRef = firestoreDatabase.collection("users").document(auth.uid.toString())

        //Fetch Profile Pic
        val Folder: StorageReference = FirebaseStorage.getInstance().getReference()
            .child("usersProfilePics/" + auth.uid.toString())
        val localFile = java.io.File.createTempFile("tempImage", "jpg")
        Folder.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            val drawable: Drawable = BitmapDrawable(resources, bitmap)
            binding.ibMyAccountMyAccountSettings.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.w(
                "DB",
                "Error al cargar la foto de perfil del usuario"
            ); Toast.makeText(baseContext, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT)
            .show()
        }
    }
}