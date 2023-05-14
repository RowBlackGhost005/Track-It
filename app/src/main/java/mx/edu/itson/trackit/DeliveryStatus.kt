package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import mx.edu.itson.trackit.data.PuntoControl


class DeliveryStatus : AppCompatActivity(), OnMapReadyCallback {

    private var lng : Double = 2.292561
    private var lat : Double = 48.858419
    private var puntos: ArrayList<PuntoControl> = ArrayList<PuntoControl>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_status)

        //carga los datos del envio en pantalla

        /**
        val dialog = BottomSheetDialog(this)
        val vista = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_resource,null)
        */

        var iconoView : ImageView = findViewById(R.id.ivStatus) as ImageView
        var trackView : TextView = findViewById(R.id.tvCode) as TextView
        var estadoView: TextView = findViewById(R.id.tvStatusDeliveryStatus) as TextView
        var destinoView : TextView = findViewById(R.id.tvDestino_deliveryStatus) as TextView

        var trkCode: String? = intent.getStringExtra("envio")
        var estado: String? = intent.getStringExtra("estado")
        var puntos = intent.getSerializableExtra("puntos") as ArrayList<PuntoControl>
        var destino: String? = intent.getStringExtra("destino")
        lng = intent.getDoubleExtra("longitud",2.292561 )
        lat = intent.getDoubleExtra("latitud",48.858419)

        trackView.setText(trkCode)
        estadoView.setText(estado)
        destinoView.setText(destino)

        if(estado.equals("en camino")){
            iconoView.setImageResource(R.drawable.transportista)
        }else if(estado.equals("entregado")){
            iconoView.setImageResource(R.drawable.entregado)
        }else {
            iconoView.setImageResource(R.drawable.enespera)
        }

        //botones para redirigir

        var ibReturnScan: ImageButton = findViewById(R.id.ibDeliveryStatus_back)

        ibReturnScan.setOnClickListener() {
            var intent: Intent = Intent(this, ScanQRCode::class.java)
            startActivity(intent)
        }


        val listView: ListView = findViewById(R.id.listViewHistorialPuntos)
        val adapter = AdaptadorBottomSheet(this, puntos)
        listView.adapter = adapter


        /**
        dialog.setCancelable(true)
        dialog.setContentView(vista)



        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
        }

        dialog.show()
        */



        //carga el mapa con las coordenadas obtenidas

        var mapFragment: SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.mapStatus) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p0: GoogleMap) {
        var mMap: GoogleMap = p0
        var posicion: LatLng = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(posicion).title("posicion"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion))
    }


    private class AdaptadorBottomSheet: BaseAdapter {
        var puntosDeControl=ArrayList<PuntoControl>()
        var contexto: Context?=null

        constructor(contexto: Context, puntosDeControl:ArrayList<PuntoControl>){
            this.puntosDeControl=puntosDeControl
            this.contexto=contexto
        }


        override fun getCount(): Int {
            return puntosDeControl.size
        }

        override fun getItem(p0: Int): Any {
            return puntosDeControl[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var punto=puntosDeControl[p0]
            var inflador= LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.bottom_sheet_details,null)

            var locacionView= vista.findViewById(R.id.tvLocacion_bottomsheet) as TextView
            var DescripcionView= vista.findViewById(R.id.tvDescripcion_bottomsheet) as TextView
            var timeView= vista.findViewById(R.id.tvTimestamp_bottomsheet) as TextView

            locacionView.setText(punto.Locacion.toString())
            DescripcionView.setText(punto.Descripcion.toString())
            timeView.setText(punto.fechaPuntoControl.toString())

            return vista

        }
    }


}