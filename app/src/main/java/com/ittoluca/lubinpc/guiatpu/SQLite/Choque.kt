package com.ittoluca.lubinpc.guiatpu.SQLite

class Choque (id:Int,id1:Int,id2:Int,Lat:Double,Long:Double,orden1:Int,orden2:Int) {
    var id=0
    var id1=0
    var id2=0
    var Lat=0.0
    var Long=0.0
    var orden1=0
    var orden2=0

    init {
       this.id=id
        this.id1=id1
        this.id2=id2
        this.Lat=Lat
        this.Long=Long
        this.orden1=orden1
        this.orden2=orden2
    }
}

class Choques (id1:Int,id2:Int,Lat:Double,Long:Double,orden1:Int,orden2:Int) {

    var id1=0
    var id2=0
    var Lat=0.0
    var Long=0.0
    var orden1=0
    var orden2=0

    init {
        this.id1=id1
        this.id2=id2
        this.Lat=Lat
        this.Long=Long
        this.orden1=orden1
        this.orden2=orden2
    }
}
