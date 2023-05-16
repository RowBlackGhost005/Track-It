package mx.edu.itson.trackit

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import mx.edu.itson.trackit.data.Usuario
import mx.edu.itson.trackit.databinding.ActivityMyAccountSettingsBinding

class MyAccountSettings : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityMyAccountSettingsBinding

    private val File = 1
    private val database = Firebase.database
    private val picsDatabase = database.getReference("usersProfilePics")
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

        binding.ibMyAccountSettingsProfilePic.setOnClickListener{
            fileUpload()
        }


    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("usersProfilePics")
                val file_name: StorageReference = Folder.child(auth.uid!!)
                updateProfilePic(file_name.path)
                file_name.putFile(FileUri!!).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->
                        val hashMap =
                            HashMap<String, String>()
                        hashMap["link"] = java.lang.String.valueOf(uri)
                        picsDatabase.setValue(hashMap)
                        Log.d("PPIC", "Foto de perfil subida correctamente")
                    }
                }
            }
        }
    }

    private fun updateProfilePic(profilePic:String){
        val firestoreDatabase =  Firebase.firestore

        val bdUsers = firestoreDatabase.collection("users").document(auth.uid!!)

        bdUsers
            .update("profilePic", profilePic)
            .addOnSuccessListener { Log.d("DB", "Foto de perfil actualizada!") ; Toast.makeText(baseContext, "Foto de perfil actualizada!", Toast.LENGTH_SHORT).show() ; fetchUserData()}
            .addOnFailureListener { e -> Log.w("DB", "Error actualizando la imágen", e) ; Toast.makeText(baseContext, "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show() }

    }
    private fun updateUserData(userUid:String, newUserName:String, newFullName:String, newEmail:String){

        if(!validateUserData(newUserName, newFullName, newEmail)){
            return
        }

        if(newEmail != auth.currentUser!!.email){
            auth.currentUser!!.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateUserDataDataBase(userUid, newUserName, newFullName, newEmail)
                        Log.d("AUTH", "User email address updated.")
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(baseContext, "Error al actualizar el correo", Toast.LENGTH_SHORT).show()
                    Log.d("AUTH", "Error al cambiar el correo del usuario.")
                }
        }else{
            updateUserDataDataBase(userUid, newUserName, newFullName, newEmail)
        }
    }

    private fun validateUserData(newUserName:String, newFullName:String, newEmail:String):Boolean{
        if(!newUserName.matches(Regex("[/^[a-zA-Z\\s]*\$/]+"))){
            Toast.makeText(baseContext, "Nombre de usuario inválido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!newFullName.matches(Regex("[/^[a-zA-Z\\s]*\$/]+"))){
            Toast.makeText(baseContext, "Nombre inválido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            Toast.makeText(baseContext, "Email inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun updateUserDataDataBase(userUid:String, newUserName:String, newFullName:String, newEmail:String){
        val firestoreDatabase =  Firebase.firestore
        val userDocument = firestoreDatabase.collection("users").document(userUid)

        userDocument
            .update("username", newUserName, "name", newFullName, "email" , newEmail)
            .addOnSuccessListener { Log.d("DB", "Datos del usuario actualizados!") ; Toast.makeText(baseContext, "Datos actualizados", Toast.LENGTH_SHORT).show() ; fetchUserData()}
            .addOnFailureListener { e -> Log.w("DB", "Error actualizando el usuario", e) ; Toast.makeText(baseContext, "Error al actualizar los datos", Toast.LENGTH_SHORT).show() }
    }

    private fun fetchUserData() {
        val firestoreDatabase = Firebase.firestore
        val docRef = firestoreDatabase.collection("users").document(auth.uid.toString())

        var user: Usuario?

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user =  document.toObject(Usuario::class.java)

                    if(user!!.profilePic != "default"){
                        loadUserProfilePic()

                    }
                    loadUserData(user)
                    Log.d("DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "get failed with ", exception)
            }
    }

    private fun loadUserProfilePic(){
        //Fetch Profile Pic
        val Folder: StorageReference = FirebaseStorage.getInstance().getReference().child("usersProfilePics/"+ auth.uid.toString())
        val localFile = java.io.File.createTempFile("tempImage" , "jpg")
        Folder.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            val drawable: Drawable = BitmapDrawable(resources, bitmap)
            binding.ibMyAccountSettingsProfilePic.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.w("DB", "Error al cargar la foto de perfil del usuario") ; Toast.makeText(baseContext, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT).show()
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