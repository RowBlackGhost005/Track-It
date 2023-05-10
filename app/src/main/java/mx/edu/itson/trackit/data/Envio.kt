package mx.edu.itson.trackit.data

data class Envio(var Id:String, var TrackId: String, var Paqueteria: String,
                 var Destino:String, var PuntosControl: ArrayList<PuntoControl>,
                 var Estado: String, var EsArchivado: Boolean)

{
    constructor() : this("0", "0", "0" , "0" ,ArrayList<PuntoControl>() , "0" , false )
}