package com.ittoluca.lubinpc.guiatpu.SQLite

import com.google.android.gms.maps.model.LatLng

class Trayecto(id_ruta:Int,Orden:Int,Lat0:Double,Long0:Double,distancia:Double,tiempo:Double,polyline:String){
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
class PuntosCortos(id_ruta:Int,Orden: Int,Punto:LatLng,PuntoF:LatLng,Distancia:Double){
    var id_ruta:Int
    var Orden: Int
    var Punto:LatLng
    var Distancia:Double
    var PuntoF:LatLng
    init {
        this.id_ruta=id_ruta
        this.Orden=Orden
        this.Punto=Punto
        this.Distancia=Distancia
        this.PuntoF=PuntoF
    }
}
