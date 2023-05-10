package mx.edu.itson.trackit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio
import mx.edu.itson.trackit.data.Usuario

class AddTrackingNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracking_number)

        var btnTrackPackage: Button = findViewById(R.id.btnAddTrackingNumber_trackPackage)

        btnTrackPackage.setOnClickListener(){

            consultaEnvios()

            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        var ibReturnMainPage: ImageButton = findViewById(R.id.ibAddTrackingNumber_back)

        ibReturnMainPage.setOnClickListener(){
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        var trackByQR: Button = findViewById(R.id.btnAddTrackingNumber_trackByQr)

        trackByQR.setOnClickListener(){
            var intent: Intent = Intent(this , ScanQRCode::class.java)
            startActivity(intent)
        }
    }


    //consulta los envio por id
    fun consultaEnvios() {

        var etTrack : EditText = findViewById(R.id.etAddTrackingNumber_trackingNumber)

        val firestoreDatabase = Firebase.firestore
        val enviosRef = firestoreDatabase.collection("envioConsumir").whereEqualTo("trackId",etTrack.text.toString()).get().addOnSuccessListener(){

            for(documentos: QueryDocumentSnapshot in it){

                guardaEnvio(documentos.toObject(Envio::class.java))

            }

        }

    }

    fun guardaEnvio(envio: Envio?) {

        val firestoreDatabase = Firebase.firestore
        val enviosRef = firestoreDatabase.collection("envios")

        //agrega el track id a la listas del usuario
        val user = Firebase.auth.currentUser

        val userRef = firestoreDatabase.collection("users").whereEqualTo("uid",user?.uid.toString()).get().addOnSuccessListener(){

            for(documentos: QueryDocumentSnapshot in it){

                val usuario: Usuario = documentos.toObject(Usuario::class.java)

                if(!usuario.parcels.contains(envio?.TrackId.toString())){

                    if (envio != null) {
                        enviosRef.add(envio)
                    }

                    actualizarUsuario(usuario,documentos.id.toString(),envio?.TrackId.toString())
                }else{
                    val toast =
                        Toast.makeText(applicationContext, "Envio ya esta registrado en la cuenta", Toast.LENGTH_SHORT)
                    toast.setMargin(50f, 50f)
                    toast.show()
                }



            }

        }

    }

    fun actualizarUsuario(usuario: Usuario,id: String,trackId: String){
        val firestoreDatabase = Firebase.firestore
        val userRef = firestoreDatabase.collection("users").document(id)

        userRef.update("parcels",FieldValue.arrayUnion(trackId))

    }

}