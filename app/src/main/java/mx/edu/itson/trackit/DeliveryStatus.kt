package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DeliveryStatus : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_status)

        var ibReturnScan: ImageButton = findViewById(R.id.ibDeliveryStatus_back)

        ibReturnScan.setOnClickListener() {
            var intent: Intent = Intent(this, ScanQRCode::class.java)
            startActivity(intent)
        }
    }
        /**
        val bottomSheet: ConstraintLayout = findViewById(R.id.delivery_status)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> Toast.makeText(
                        this@DeliveryStatus,
                        "STATE_COLLAPSED",
                        Toast.LENGTH_SHORT
                    )
                    BottomSheetBehavior.STATE_EXPANDED -> Toast.makeText(
                        this@DeliveryStatus,
                        "STATE_EXPANDED",
                        Toast.LENGTH_SHORT
                    )
                    BottomSheetBehavior.STATE_DRAGGING -> Toast.makeText(
                        this@DeliveryStatus,
                        "STATE_DRAGGING",
                        Toast.LENGTH_SHORT
                    )
                    BottomSheetBehavior.STATE_SETTLING -> Toast.makeText(
                        this@DeliveryStatus,
                        "STATE_SETTLING",
                        Toast.LENGTH_SHORT
                    )
                    BottomSheetBehavior.STATE_HIDDEN -> Toast.makeText(
                        this@DeliveryStatus,
                        "STATE_HIDDEN",
                        Toast.LENGTH_SHORT
                    )
                    else -> Toast.makeText(this@DeliveryStatus, "OTHER_STATE", Toast.LENGTH_SHORT)
                }
            }
        })

        val btnBottomSheetPersistent: Button = findViewById(R.id.cibDeliveryStatus_details)

        btnBottomSheetPersistent.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            fullHeight(bottomSheet)
        }
    }

    private fun fullHeight(bottomSheet: View) {
        var layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }**/
}