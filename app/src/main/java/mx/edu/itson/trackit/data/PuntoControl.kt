package mx.edu.itson.trackit.data

import java.util.Date

data class PuntoControl(var Id: String, var fechaPuntoControl: Date,
                        var Locacion: String,var Descripcion: String)

{
    constructor() : this("0", Date(), "0" , "0" )
}