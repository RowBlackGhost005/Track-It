package mx.edu.itson.trackit.data

data class Envio(var Id:String?, var TrackId: String, var Paqueteria: String,
                 var Destino:String, var PuntosControl: String,
                 var Estado: String, var EsArchivado: Boolean)
