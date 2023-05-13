package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import mx.edu.itson.trackit.data.Envio
import mx.edu.itson.trackit.data.PuntoControl
import org.w3c.dom.Text
import java.io.Serializable

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

        var iconoView : ImageView = this.findViewById(R.id.ivStatus) as ImageView
        var trackView : TextView = this.findViewById(R.id.tvCode) as TextView
        var estadoView: TextView = this.findViewById(R.id.tvStatusDeliveryStatus) as TextView
        var destinoView : TextView = this.findViewById(R.id.tvDestino_deliveryStatus) as TextView

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

        var btnHistorial : Button = findViewById(R.id.btnHistorialPuntos)

        btnHistorial.setOnClickListener(View.OnClickListener {

            val dialog = BottomSheetDialog(this)
            val vista = layoutInflater.inflate(R.layout.bottom_dialog_resource,null)


            dialog.setCancelable(true)
            dialog.setContentView(vista)

            var listView: ListView = vista.findViewById(R.id.listViewHistorialPuntos)
            var adaptador: DeliveryStatus.AdaptadorBottomSheet = DeliveryStatus.AdaptadorBottomSheet(this,puntos)
            listView.adapter=adaptador

            dialog.show()
        })


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