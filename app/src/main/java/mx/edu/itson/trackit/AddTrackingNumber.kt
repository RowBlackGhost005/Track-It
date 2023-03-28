package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AddTrackingNumber : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracking_number)

        var btnTrackPackage: Button = findViewById(R.id.btnAddTrackingNumber_trackPackage)

        btnTrackPackage.setOnClickListener(){
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
}