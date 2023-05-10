package mx.edu.itson.trackit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ImageButton

class Frequently_asked : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frequently_asked)

        var myTrackings: ImageButton = findViewById(R.id.ibFrqAsk_myTrackings)

        myTrackings.setOnClickListener(){
            var intent: Intent = Intent(this , MainPage::class.java)
            startActivity(intent)
        }

        var myAccount: ImageButton = findViewById(R.id.ibFrqAsk_myAccount)

        myAccount.setOnClickListener(){
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }

        var expandableListView: ExpandableListView = findViewById(R.id.elvFrqAsk)

        var items = HashMap<String,List<String>>()

        var grupo1 = ArrayList<String>()
        grupo1.add("Regularmente si el paquete ya ha sido enviado, esa dirección" +
                "no puede ser alterada. Puede esperar hasta que la carga llegue a su país" +
                "y entonces solicitar el cambio de dirección al transportistas local. Para ello," +
                "vaya a la página web del transportista.")

        items.put("¿Puedo solicitar un cambio de dirección?",grupo1)

        var grupo2 = ArrayList<String>()
        grupo2.add("Texto de ejemplo")

        items.put("Ejemplo",grupo1)


        val Adapter = ExpandableListAdapter(items)
        expandableListView.setAdapter(Adapter)


    }


}