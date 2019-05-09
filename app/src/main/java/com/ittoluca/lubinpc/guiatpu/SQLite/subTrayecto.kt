package com.ittoluca.lubinpc.guiatpu.SQLite

class subTrayecto(id_ruta:Int,Orden:Int,subOrden:Int,Lat0:Double,Long0:Double){
    var id_ruta:Int?=null
    var Orden:Int?=null
    var subOrden:Int?=null
    var Lat0:Double?=null
    var Long0:Double?=null


    init {
        this.id_ruta=id_ruta
        this.Orden=Orden
        this.subOrden=subOrden
        this.Lat0=Lat0
        this.Long0=Long0

    }
}