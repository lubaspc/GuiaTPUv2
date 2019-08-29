package com.ittoluca.lubinpc.guiatpu.SQLite

import android.graphics.Bitmap

class Rutas(id_ruta:Int,nombre:String,costo:Double,Foto:Bitmap,Color:String,JSON:String) {
    var id_ruta:Int?=null
    var nombre:String?=null
    var Foto:Bitmap?=null
    var Color:String?=null
    var costo:Double?=null
    var JSON:String?=null

    init {
        this.id_ruta=id_ruta
        this.nombre=nombre
        this.Foto=Foto
        this.Color=Color
        this.costo=costo
        this.JSON=JSON
    }
}

class RutasI(nombre:String,costo:Double,Foto:Bitmap,Color:String,JSON:String) {

    var nombre:String?=null
    var Foto:Bitmap?=null
    var Color:String?=null
    var costo:Double?=null
    var JSON:String?=null

    init {
        this.nombre=nombre
        this.Foto=Foto
        this.Color=Color
        this.costo=costo
        this.JSON=JSON
    }
}
