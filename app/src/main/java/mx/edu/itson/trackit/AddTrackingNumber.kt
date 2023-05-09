package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio
import okhttp3.*
import java.io.IOException

class AddTrackingNumber : AppCompatActivity() {

    val client = OkHttpClient()
    private val database = Firebase.firestore
    private val enviosRef = database.collection("envios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracking_number)

        var btnTrackPackage: Button = findViewById(R.id.btnAddTrackingNumber_trackPackage)

        btnTrackPackage.setOnClickListener(){

            this.run()

            //tracking number de prueba
            //this.addTracking("00340434292135100186")

            //var intent: Intent = Intent(this , MainPage::class.java)
            //startActivity(intent)
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

    fun addTracking(number: String){
       var trkNumber = number

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api-test.dhl.com/track/shipments?trackingNumber= "+trkNumber)
            .get()
            .addHeader("DHL-API-Key", "uaJIX8pdM34mMAlFHRkMJqcXyuCK2lRg")
            .build()


        val response = client.newCall(request).execute()
        println(response.body.toString())

    }

    fun run() {
        val request = Request.Builder()
            .url("https://api-eu.dhl.com/track/shipments?trackingNumber=12345678910")
            .get()
            .addHeader("DHL-API-Key","uaJIX8pdM34mMAlFHRkMJqcXyuCK2lRg")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    var trk: EditText = findViewById(R.id.etAddTrackingNumber_trackingNumber) as EditText

                    val bodyString: String? = response.body?.string()

                    println(bodyString)

/*
                    var TrackId = trk.text
                    var Paqueteria = "DHL"
                    var Destino =
                    var PuntosControl =
                    var Estado = bodyString
                    var EsArchivado =

                    val envio = Envio(

                    )


                    
*/
                }
            }
        })

        var intent: Intent = Intent(this , tracking_menu::class.java)
        startActivity(intent)
    }
}