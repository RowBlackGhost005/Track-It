package mx.edu.itson.trackit.data

data class Usuario(var uid: String,var username: String,
                   var name: String, var email:String,
                   var parcels: ArrayList<String>, var profilePic: String,
                   var isActivated: Boolean, var activateCode:Int)
{
    constructor() : this("0", "0", "0" , "0" , ArrayList<String>() , "0" , false , 0)
}