package mx.edu.itson.trackit.data

data class Envio(var Id:Int, var TrackId: Int, var Paqueteria: String,
                 var Destino:String, var PuntosControl: String,
                 var Estado: String, var EsArchivado: Boolean)
