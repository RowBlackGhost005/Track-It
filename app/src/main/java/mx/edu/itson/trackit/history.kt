package mx.edu.itson.trackit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio
import mx.edu.itson.trackit.data.RelacionArchivado
import mx.edu.itson.trackit.data.Usuario
import mx.edu.itson.trackit.databinding.ActivityMainPageBinding

class history : AppCompatActivity() {

    private var comprobar: Int? = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainPageBinding
    //contiene objetos tipo envio
    var parcelsobj: ArrayList<Envio> =ArrayList()
    //lista de a
    var parcels: ArrayList<RelacionArchivado>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

      //  var menuOption: String? = intent.getStringExtra("menuType")
        searchUserParcels()

        var btnLimpiar = findViewById(R.id.btnHistoryEliminar) as Button

        btnLimpiar.setOnClickListener{
            limpiarHistorial()
        }
    }

    fun limpiarHistorial(){
        var contador : Int = 0
        parcelsobj.let{
            for(x in it){
                val user = Firebase.auth.currentUser
                val firestoreDatabase = Firebase.firestore
                val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())

                var relacionArchivado = RelacionArchivado(x.TrackId,true)

                userRef.update("parcels",FieldValue.arrayRemove(relacionArchivado))
                    .addOnSuccessListener {

                        Log.d("DB", "se ha eliminado con exito")

                        parcelsobj.clear()

                        var listView: ListView =findViewById(R.id.lvHistory) as ListView
                        var adaptador: history.AdaptadorRastreos = history.AdaptadorRastreos(this, parcelsobj)
                        listView.adapter=adaptador

                    }
                    .addOnFailureListener { exception ->
                        Log.d("DB", "no se ha podido eliminar ")
                    }




            }
        }
    }

    //agrega los rastreos y los aniade a la lista que se muestra en pantalla
    fun agregarRastreos(){

        var listView: ListView =findViewById(R.id.lvHistory) as ListView
        var adaptador: history.AdaptadorRastreos = history.AdaptadorRastreos(this, parcelsobj)
        listView.adapter=adaptador

    }


    //consulta los envios de un usuario en particular
    fun consultaEnvios() {

        val firestoreDatabase = Firebase.firestore
        var contador = 0

        parcels?.let {
            for (x in it){

                val index = it.indexOf(x)
                val envioRef = firestoreDatabase.collection("envios").document(parcels?.get(index)?.trackId.toString())

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

                    list?.let {
                        for (x in it) {
                            var relacionArchivado: RelacionArchivado = x

                            if(relacionArchivado.esArchivado){
                                this.parcels?.add(relacionArchivado)
                            }else{

                            }

                        }
                    }

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

            var imageButton = vista.findViewById(R.id.ibStatusTracking_Settings) as ImageView

            imageButton.setOnClickListener {
                val popupMenu = PopupMenu(contexto, imageButton)
                popupMenu.inflate(R.menu.menueliminar)

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.botonEliminar -> {

                            val user = Firebase.auth.currentUser
                            val firestoreDatabase = Firebase.firestore
                            val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())


                            var relacionArchivado = RelacionArchivado(trk.TrackId,true)

                            userRef.update("parcels",FieldValue.arrayRemove(relacionArchivado))
                                .addOnSuccessListener {
                                    var intent: Intent = Intent(contexto , history::class.java)
                                    contexto?.startActivity(intent)

                                    Log.d("DB", "se ha eliminado con exito")

                                }
                                .addOnFailureListener { exception ->
                                    Log.d("DB", "no se ha podido eliminar ")
                                }



                            true
                        }
                        else -> false
                    }
                }

                popupMenu.show()
            }




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