package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio
import mx.edu.itson.trackit.data.PuntoControl
import mx.edu.itson.trackit.data.RelacionArchivado
import mx.edu.itson.trackit.data.Usuario

class tracking_menu : AppCompatActivity() {

    //contiene objetos tipo envio
    var parcelsobj: ArrayList<Envio> =ArrayList()
    //lista de a
    var parcels: ArrayList<String>? = ArrayList()
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

        //busca los envios del usuario activo
        this.searchUserParcels()



    }

    //agrega los rastreos y los aniade a la lista que se muestra en pantalla
    fun agregarRastreos(){

        var listView: ListView =findViewById(R.id.listViewTrackingMenu) as ListView
        var adaptador: tracking_menu.AdaptadorRastreos = tracking_menu.AdaptadorRastreos(this, parcelsobj)
        listView.adapter=adaptador

    }


    //consulta los envios de un usuario en particular
    fun consultaEnvios() {

        val firestoreDatabase = Firebase.firestore
        var contador = 0

        parcels?.let {
            for (x in it){

                val index = it.indexOf(x)
            val envioRef = firestoreDatabase.collection("envios").document(parcels?.get(index).toString())

            envioRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val envio: Envio? = document.toObject(Envio::class.java)

                        parcelsobj?.add(envio!!)

                        contador++

                        if(contador == it.size){
                            agregarRastreos()
                        }

                        Log.d("DB", "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d("DB", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("DB", "get failed with ", exception)
                }

            }

        }
    }

    //obtiene los id de envios del usuario activo
    fun searchUserParcels(){

        //obtiene el usuario activo
        val user = Firebase.auth.currentUser

        val firestoreDatabase = Firebase.firestore
        val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val usuario: Usuario? = document.toObject(Usuario::class.java)

                    var list: ArrayList<RelacionArchivado>? = usuario?.parcels

                    //this.parcels = list

                    consultaEnvios()

                    Log.d("DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB", "get failed with ", exception)
            }
    }


    private class AdaptadorRastreos: BaseAdapter {
        var rastreos=ArrayList<Envio>()
        var contexto: Context?=null

        constructor(contexto: Context, rastreos:ArrayList<Envio>){
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

            vista.setOnClickListener(){
                var intent: Intent = Intent(contexto , DeliveryStatus::class.java)

                var cargarEnvio = rastreos.get(p0)

                intent.putExtra("envio",cargarEnvio.TrackId)
                intent.putExtra("estado",cargarEnvio.Estado)
                intent.putExtra("puntos",cargarEnvio.PuntosControl)
                intent.putExtra("destino",cargarEnvio.Destino)
                intent.putExtra("latitud",cargarEnvio.coordenadas.latitude)
                intent.putExtra("longitud",cargarEnvio.coordenadas.longitude)

                contexto!!.startActivity(intent)
            }

            var imagen= vista.findViewById(R.id.ivStatusTracking_Logo) as ImageView
            var codigoRastreo= vista.findViewById(R.id.tvTrackingMenu_title) as TextView
            var direccion= vista.findViewById(R.id.tvTrackingObject_adress) as TextView
            var status= vista.findViewById(R.id.tvTrackingObject_status) as TextView
            var carrier= vista.findViewById(R.id.tvTrackingObject_status2) as TextView

            var icono: Int

            if(trk.Estado.equals("en camino")){
                icono = R.drawable.transportista
            }else if(trk.Estado.equals("entregado")){
                icono = R.drawable.entregado
            }else{
                icono = R.drawable.enespera
            }


            imagen.setImageResource(icono)
            codigoRastreo.setText(trk.TrackId)
            direccion.setText(trk.Destino)
            status.setText(trk.Estado)
            carrier.setText(trk.Paqueteria)
            return vista

        }
    }
}