package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class shipper : AppCompatActivity() {

    var transportistas=ArrayList<Carrier>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipper)


        var botonNacional: Button = findViewById(R.id.btnShipper_National)
        var botonInternacional: Button = findViewById(R.id.btnShipper_International)
        var listView: ListView =findViewById(R.id.lvCarrier) as ListView

        botonNacional.setOnClickListener{
            transportistas.clear()
            agregarTransportistas("nacional")
            listView.invalidateViews();

        }

        botonInternacional.setOnClickListener{
            transportistas.clear()
            agregarTransportistas("internacional")
            listView.invalidateViews();

        }

        var optionTipo: String? = intent.getStringExtra("tipo")
        agregarTransportistas(optionTipo)

        var adaptador:AdaptadorTransportistas= AdaptadorTransportistas(this,transportistas)
        listView.adapter=adaptador
    }

    fun agregarTransportistas(optionTipo: String?){

        var botonNacional: Button = findViewById(R.id.btnShipper_National)
        var botonInternacional: Button = findViewById(R.id.btnShipper_International)

        when(optionTipo){
            "nacional" -> {
                transportistas.add(Carrier("DHL México"))
                transportistas.add(Carrier("Estafeta México"))
                transportistas.add(Carrier("UPS México"))
                transportistas.add(Carrier("Correos de México"))
                transportistas.add(Carrier("Paquete Express"))

                botonInternacional.setEnabled(true)

                botonNacional.setBackgroundColor(getResources().getColor(R.color.purple_200))
                botonInternacional.setBackgroundColor(getResources().getColor(R.color.mainTextColor))

                botonNacional.setEnabled(false)
            }

            "internacional" -> {
                transportistas.add(Carrier("DHL"))
                transportistas.add(Carrier("Estafeta"))
                transportistas.add(Carrier("UPS"))
                transportistas.add(Carrier("FedEx"))
                transportistas.add(Carrier("Mercado Envíos"))
                transportistas.add(Carrier("Amazon Prime"))

                botonNacional.setEnabled(true)

                botonNacional.setBackgroundColor(getResources().getColor(R.color.mainTextColor))
                botonInternacional.setBackgroundColor(getResources().getColor(R.color.purple_200))

                botonInternacional.setEnabled(false)
            }

            else -> {
                transportistas.add(Carrier("DHL México"))
                transportistas.add(Carrier("Estafeta México"))
                transportistas.add(Carrier("UPS México"))
                transportistas.add(Carrier("UPS México"))
                transportistas.add(Carrier("UPS México"))
                transportistas.add(Carrier("UPS México"))
                transportistas.add(Carrier("UPS México"))

                botonInternacional.setEnabled(true)

                botonNacional.setBackgroundColor(getResources().getColor(R.color.purple_200))
                botonInternacional.setBackgroundColor(getResources().getColor(R.color.mainTextColor))

                botonNacional.setEnabled(false)
            }
        }

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