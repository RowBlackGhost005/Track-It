package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ScanQRCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)

        var ibReturnTracking: ImageButton = findViewById(R.id.ibScanQRCode_back)

        ibReturnTracking.setOnClickListener(){
            var intent: Intent = Intent(this , AddTrackingNumber::class.java)
            startActivity(intent)
        }

        var btnReturnMainPage: Button = findViewById(R.id.btnScanQRCode_trackPackage)

        btnReturnMainPage.setOnClickListener(){
            var intent: Intent = Intent(this , DeliveryStatus::class.java)
            startActivity(intent)
        }

    }
}