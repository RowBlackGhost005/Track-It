package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio

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
        val enviosRef = firestoreDatabase.collection("envios").document(etTrack.text.toString())

        enviosRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val envio: Envio? = document.toObject(Envio::class.java)

                    guardaEnvio(envio)

                    Log.d("DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "get failed with ", exception)
            }

    }

    fun guardaEnvio(envio: Envio?) {

        val firestoreDatabase = Firebase.firestore
        val enviosRef = firestoreDatabase.collection("envios")

        if (envio != null) {
            enviosRef.add(envio)
        }

    }

}