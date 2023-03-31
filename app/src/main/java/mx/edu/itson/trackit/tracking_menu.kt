package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class tracking_menu : AppCompatActivity() {

    var rastreos=ArrayList<Tracking>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_menu)

        var myTrackings: ImageButton = findViewById(R.id.ibTrackingMenu_myTrackings)

        myTrackings.setOnClickListener(){
            var intent: Intent = Intent(this , tracking_menu::class.java)
            startActivity(intent)
        }

        var myAccount: ImageButton = findViewById(R.id.ibTrackingMenu_myAccount)

        myAccount.setOnClickListener(){
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }

        var btnAniadirCodigo: Button = findViewById(R.id.btnAddTracking_add)

        btnAniadirCodigo.setOnClickListener(){
            var intent: Intent = Intent(this , AddTrackingNumber::class.java)
            startActivity(intent)
        }

        agregarRastreos()

        var listView: ListView =findViewById(R.id.listViewTrackingMenu) as ListView
        var adaptador: tracking_menu.AdaptadorRastreos = tracking_menu.AdaptadorRastreos(this, rastreos)
        listView.adapter=adaptador


    }

    fun agregarRastreos(){
        rastreos.add(Tracking("codigo: 508462549845R","Guerrero 251","Entregado el 12/12/12",
            "enviado por dhl",R.drawable.transportista))
        rastreos.add(Tracking("codigo: 508462549845R","Guerrero 251","Entregado el 12/12/12",
            "enviado por dhl",R.drawable.transportista))
    }

    private class AdaptadorRastreos: BaseAdapter {
        var rastreos=ArrayList<Tracking>()
        var contexto: Context?=null

        constructor(contexto: Context, rastreos:ArrayList<Tracking>){
            this.rastreos=rastreos
            this.contexto=contexto
        }


        override fun getCount(): Int {
            return rastreos.size
        }

        override fun getItem(p0: Int): Any {
            return rastreos[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var trk=rastreos[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.rastreo_view,null)

            var imagen= vista.findViewById(R.id.ivStatusTracking_Logo) as ImageView
            var codigoRastreo= vista.findViewById(R.id.tvTrackingMenu_title) as TextView
            var direccion= vista.findViewById(R.id.tvTrackingObject_adress) as TextView
            var status= vista.findViewById(R.id.tvTrackingObject_status) as TextView
            var carrier= vista.findViewById(R.id.tvTrackingObject_status2) as TextView

            imagen.setImageResource(trk.image)
            codigoRastreo.setText(trk.TrackingCode)
            direccion.setText(trk.adress)
            status.setText(trk.status)
            carrier.setText(trk.carrier)
            return vista

        }
    }
}