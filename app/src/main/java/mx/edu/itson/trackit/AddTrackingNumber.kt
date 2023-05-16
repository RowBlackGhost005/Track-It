package mx.edu.itson.trackit

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import mx.edu.itson.trackit.data.RelacionArchivado
import mx.edu.itson.trackit.data.Usuario

class AddTrackingNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracking_number)

        var btnTrackPackage: Button = findViewById(R.id.btnAddTrackingNumber_trackPackage)

        btnTrackPackage.setOnClickListener(){

            consultaEnvios()

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
    fun consultaEnvios(){

        var etTrack : EditText = findViewById(R.id.etAddTrackingNumber_trackingNumber)

        val firestoreDatabase = Firebase.firestore

        val enviosRef = firestoreDatabase.collection("envioConsumir").document(etTrack.text.toString())

        enviosRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val envio: Envio? = document.toObject(Envio::class.java)

                    try{
                        var comprobar= !envio?.TrackId!!.isEmpty()
                        verificaEnvio(envio,etTrack.text.toString())

                    }catch (e: Exception){
                        val toast =
                            Toast.makeText(applicationContext, "No se ha encontrado el envio", Toast.LENGTH_SHORT)
                        toast.setMargin(50f, 50f)
                        toast.show()
                    }


                        Log.d("DB", "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d("DB", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DB", "get failed with ", exception)
                }


    }

    fun verificaEnvio(envio: Envio?,id: String?) {

        val firestoreDatabase = Firebase.firestore


        //agrega el track id a la listas del usuario
        val user = Firebase.auth.currentUser


        val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())

        //obtiene el usuario para verificar si ya tiene el objeto
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val usuario: Usuario? = document.toObject(Usuario::class.java)
                    //si no contiene, entonces procede a agregar envio
                    var relacionArchivado = RelacionArchivado(id.toString(),false)
                    var relacionArchivado2 = RelacionArchivado(id.toString(),true)

                    if(!usuario?.parcels!!.contains(relacionArchivado) && !usuario?.parcels!!.contains(relacionArchivado2)){

                        guardaEnvio(envio,id)

                    }else{
                        //muestra mensaje de que ya se contiene el envio
                        val toast =
                            Toast.makeText(applicationContext, "Envio ya esta registrado en la cuenta", Toast.LENGTH_SHORT)
                        toast.setMargin(50f, 50f)
                        toast.show()
                    }

                    Log.d("DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "get failed with ", exception)
            }

    }

    fun guardaEnvio(envio: Envio?,id:String?){
        val user = Firebase.auth.currentUser
        val firestoreDatabase = Firebase.firestore
        val enviosRef = firestoreDatabase.collection("envios").document(id.toString())

        //verifica si el envio ya se encuentra en la coleccion
        enviosRef.get().addOnSuccessListener { document ->
            if(document != null){

                if (envio != null) {
                    enviosRef.set(envio)
                }

                actualizarUsuario(user?.uid.toString(),id.toString())

                var intent: Intent = Intent(this , MainPage::class.java)
                startActivity(intent)

                Log.d("DB", "DocumentSnapshot data: ${document.data}")
            }else{
                actualizarUsuario(user?.uid.toString(),id.toString())
                Log.d("DB", "No such document")
            }
        }
    }

    fun actualizarUsuario(id: String,trackId: String){
        val firestoreDatabase = Firebase.firestore
        val userRef = firestoreDatabase.collection("users").document(id)


        var relacionArchivado = RelacionArchivado(trackId,false)

        userRef.update("parcels",FieldValue.arrayUnion(relacionArchivado))
            .addOnSuccessListener {
                Log.d("DB", "Elemento agregado correctamente a la lista")
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "Error al agregar el elemento a la lista: $exception")
            }

    }

}