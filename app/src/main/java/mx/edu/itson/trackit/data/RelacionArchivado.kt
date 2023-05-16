package mx.edu.itson.trackit.data

data class RelacionArchivado(var trackId:String,
                             var esArchivado:Boolean)

{
    constructor() : this("0", false)
}