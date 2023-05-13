package mx.edu.itson.trackit.data

import java.io.Serializable
import java.util.Date

data class PuntoControl(var Id: String, var fechaPuntoControl: Date,
                        var Locacion: String,var Descripcion: String): Serializable

{
    constructor() : this("0", Date(), "0" , "0" )
}