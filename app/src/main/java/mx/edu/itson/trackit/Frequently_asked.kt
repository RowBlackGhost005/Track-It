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
        grupo2.add("Verifica el estado de tu paquete en el apartado de \"rastrear mi envío\"" +
                "si aún no ha sido entregado, deberas esperar hasta la fecha de entrega que la " +
                "paquetería haya proporcionado." +
                "En caso de que el estado del paquete sea entregado, trata de preguntar a un familiar " +
                "si no ha recolectado un paquete a tu nombre o a algún vecino, algunas paqueterías " +
                "tratarán de dejarlo con alguien más si así lo has indicado." +
                "Si aún no encuentras tu paquete, trata de comunicarte directamente con la paquetería " +
                "dirigiendote al apartado de \"transportistas\"")
        items.put("No he recibido mi paquete, ¿Qué debo hacer?", grupo2)

        var grupo3 = ArrayList<String>()
        grupo3.add("Esto se puede deber a múltiples factores, el principal es que tu paquete se ha " +
                "registrado pero aún no ha sido entregado a la empresa por parte de la persona que " +
                "lo va a envíar, en este caso sugerimos esperar o ponerte en contacto con la persona/empresa " +
                "que enviará tu paquete.")
        items.put("¿Qué hago si mi paquete lleva \"en espera\" por mucho tiempo?", grupo3)

        var grupo4 = ArrayList<String>()
        grupo4.add("Track-It! trabaja totalmente en español y toda su información relevante se encuentra en español.")
        items.put("¿En qué idiomas se encuentra disponible?", grupo4)

        var grupo5 = ArrayList<String>()
        grupo5.add("Si requiere asistencia técnica relacionada con Track-It! puede dirigirse al sitio web" +
                " https://trackit.com.mx/asistencia para solucionar cualquier duda o inconveniente que tenga.")
        items.put("¿Cómo puedo contactar con servicio al cliente?", grupo5)

        var grupo6 = ArrayList<String>()
        grupo6.add("El estado del paquete se puede consultar en el apartado de \"rastrear mi envío\", en él " +
                "se pueden agregar los múltiples códigos de rastreo de una gran catálogo de transportistas nacionales e internacionales.")
        items.put("Estado del paquete", grupo6)

        var grupo7 = ArrayList<String>()
        grupo7.add("Una vez que tu paquete haya sido entregado, puedes archivar dicho seguimiento para que deje " +
                "de aparecer en la pantalla de inicio, siempre podras consultarlo en el apartado de \"historial de rastreos\" " +
                "a menos que desees eliminar dicho estado del seguimiento para siempre.")
        items.put("Estado del seguimiento", grupo7)


        val Adapter = ExpandableListAdapter(items)
        expandableListView.setAdapter(Adapter)


    }


}