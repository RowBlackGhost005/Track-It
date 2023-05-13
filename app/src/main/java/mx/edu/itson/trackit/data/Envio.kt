package mx.edu.itson.trackit.data

import com.google.firebase.firestore.GeoPoint

data class Envio(var Id:String, var TrackId: String, var Paqueteria: String,
                 var Destino:String, var PuntosControl: ArrayList<PuntoControl>,
                 var Estado: String, var EsArchivado: Boolean,var coordenadas: GeoPoint)

{
    constructor() : this("0", "0", "0" , "0" ,ArrayList<PuntoControl>() ,
        "0" , false , GeoPoint(27.936494, -110.933373)
    )
}