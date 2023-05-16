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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.edu.itson.trackit.data.Envio
import mx.edu.itson.trackit.data.RelacionArchivado
import mx.edu.itson.trackit.data.Usuario
import mx.edu.itson.trackit.databinding.ActivityMainPageBinding


class MainPage : AppCompatActivity() {

    private var comprobar: Int? = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainPageBinding
    //contiene objetos tipo envio
    var parcelsobj: ArrayList<Envio> =ArrayList()
    //lista de a
    var parcels: ArrayList<RelacionArchivado>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        verificaEnvios()

        var btnAddTrackingNumber: Button = findViewById(R.id.btnMainPage_addParcel)

        btnAddTrackingNumber.setOnClickListener(){
            var intent: Intent = Intent(this , AddTrackingNumber::class.java)
            startActivity(intent)
        }

        /**
        var myTrackings: ImageButton = findViewById(R.id.ibMainPage_myTrackings)

        myTrackings.setOnClickListener(){
            var myLayout = findViewById<LinearLayout>(R.layout.activity_main_page)
            myLayout.invalidate()
        }
        */



        var myAccount: ImageButton = findViewById(R.id.ibMainPage_myAccount)

        myAccount.setOnClickListener(){
            var intent: Intent = Intent(this , MyAccount::class.java)
            startActivity(intent)
        }

    }

    //metodo que verifica si el usuario tiene envios registrados en su cuenta

    fun verificaEnvios(){
        val user = Firebase.auth.currentUser
        val firestoreDatabase = Firebase.firestore
        val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val usuario: Usuario? = document.toObject(Usuario::class.java)


                    if(!usuario?.parcels!!.isEmpty()){

                        var img : ImageView = findViewById(R.id.ivMainPage_parcels)
                        var txt : TextView = findViewById(R.id.tvMainPage_noParcelRegisted)
                        var lv : ListView = findViewById(R.id.listViewMainPage)
                        txt.visibility = View.GONE
                        img.visibility = View.GONE
                        lv.visibility = View.VISIBLE

                        this.searchUserParcels()
                    }else{
                        var img : ImageView = findViewById(R.id.ivMainPage_parcels)
                        var txt : TextView = findViewById(R.id.tvMainPage_noParcelRegisted)
                        var lv : ListView = findViewById(R.id.listViewMainPage)
                        lv.visibility = View.GONE
                        txt.visibility = View.VISIBLE
                        img.visibility = View.VISIBLE


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

    //agrega los rastreos y los aniade a la lista que se muestra en pantalla
    fun agregarRastreos(){

        var listView: ListView =findViewById(R.id.listViewMainPage) as ListView
        var adaptador: MainPage.AdaptadorRastreos = MainPage.AdaptadorRastreos(this, parcelsobj)
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

                              }else{
                                  this.parcels?.add(relacionArchivado)
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

        fun actualizarUsuario(envios: ArrayList<RelacionArchivado>?,trk:String){


            val user = Firebase.auth.currentUser
            val firestoreDatabase = Firebase.firestore
            val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())
            var relacionArchivado= RelacionArchivado(trk , false)

            if (envios!!.contains(relacionArchivado)) {
                userRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val userData = documentSnapshot.toObject(Usuario::class.java)
                            val parcelsList = userData?.parcels ?: ArrayList()

                            val actualizar = parcelsList.indexOf(relacionArchivado)
                            if (actualizar != -1) {
                                val elementoActualizado = RelacionArchivado(trk, true)
                                parcelsList[actualizar] = elementoActualizado

                                userRef.update("parcels", parcelsList)
                                    .addOnSuccessListener {

                                        Log.d("DB", "Elemento actualizado correctamente en la lista")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d("DB", "Error al actualizar el elemento en la lista: $exception")
                                    }




                            } else {
                                Log.d("DB", "No se encontró el paquete en la lista")
                            }
                        } else {
                            Log.d("DB", "No se encontró el documento del usuario")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("DB", "Error al obtener el documento del usuario: $exception")
                    }
            } else {
                Log.d("DB", "No se encontró el paquete")
            }




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
                popupMenu.inflate(R.menu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.botonArchivar -> {

                            val user = Firebase.auth.currentUser
                            val firestoreDatabase = Firebase.firestore
                            val userRef = firestoreDatabase.collection("users").document(user?.uid.toString())

                            userRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val usuario: Usuario? = document.toObject(Usuario::class.java)

                                        var list: ArrayList<RelacionArchivado>? = usuario?.parcels

                                        actualizarUsuario(list,trk.TrackId)


                                        Log.d("DB", "DocumentSnapshot data: ${document.data}")
                                    } else {
                                        Log.d("DB", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("DB", "get failed with ", exception)
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