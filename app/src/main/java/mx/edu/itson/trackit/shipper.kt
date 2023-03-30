package mx.edu.itson.trackit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class shipper : AppCompatActivity() {

    var transportistas=ArrayList<Carrier>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipper)

        agregarTransportistas()

        var listView: ListView =findViewById(R.id.lvCarrier) as ListView
        var adaptador:AdaptadorTransportistas= AdaptadorTransportistas(this,transportistas)
        listView.adapter=adaptador
    }

    fun agregarTransportistas(){
        transportistas.add(Carrier("DHL México"))
        transportistas.add(Carrier("Estafeta México"))
        transportistas.add(Carrier("UPS México"))
        transportistas.add(Carrier("UPS México"))
        transportistas.add(Carrier("UPS México"))
        transportistas.add(Carrier("UPS México"))
        transportistas.add(Carrier("UPS México"))
    }

    private class AdaptadorTransportistas: BaseAdapter {
        var transportistas=ArrayList<Carrier>()
        var contexto: Context?=null

        constructor(contexto: Context, transportistas:ArrayList<Carrier>){
            this.transportistas=transportistas
            this.contexto=contexto
        }


        override fun getCount(): Int {
            return transportistas.size
        }

        override fun getItem(p0: Int): Any {
            return transportistas[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var carrier=transportistas[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.carrier_view,null)

            var codigoRastreo= vista.findViewById(R.id.tvCarrierView_carrier) as TextView

            codigoRastreo.setText(carrier.name)
            return vista

        }
    }
}