package com.ittoluca.lubinpc.guiatpu.SQLite

class Trayecto(id_ruta:Int,Orden:Int,Lat0:Double,Long0:Double,Lat1:Double,Long1:Double,distancia:Double,tiempo:Double,polyline:String){
        var id_ruta:Int?=null
        var Orden:Int?=null
        var Lat0:Double?=null
        var Long0:Double?=null
        var Lat1:Double?=null
        var Long1:Double?=null
        var distancia:Double?=null
        var tiempo:Double?=null
        var polyline:String?=null

        init {
            this.id_ruta=id_ruta
            this.Orden=Orden
            this.Lat0=Lat0
            this.Long0=Long0
            this.Lat1=Lat1
            this.Long1=Long1
            this.distancia=distancia
            this.tiempo=tiempo
            this.polyline=polyline
        }
    }
