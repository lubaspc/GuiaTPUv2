package com.ittoluca.lubinpc.guiatpu

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import org.json.JSONObject

class RutasInf : AppCompatActivity() {
    var mMap: GoogleMap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutas_inf)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapinfo) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            mMap = Mapa(it, this).Iniialicacion()
            var id=intent.getStringExtra("id")
            val Tnombre=findViewById<TextView>(R.id.TNombre)
            val TCosto=findViewById<TextView>(R.id.TCosto)
            val TDistacia=findViewById<TextView>(R.id.TDistancia)
            val TTiempo=findViewById<TextView>(R.id.TTiempo)
            val Bcolor=findViewById<Button>(R.id.Bcolor)
            val img=findViewById<ImageView>(R.id.Img)


            var arrayR= CRUD(this).consultarRutasxID(id)
            var arrayT= CRUD(this).ConsutaTrayectoxID(id)
            var distancia=0.0
            var tiempo=0.0

            for (i in arrayT){
                distancia+= i.distancia!!
                tiempo+=i.tiempo!!
            }
            TDistacia.text=distancia.toString() + " Kilometros, Totales"
            TTiempo.text=tiempo.toString()+" Minutos"

            for (i in arrayR) {
                Tnombre.text = i.nombre
                TCosto.text = i.costo.toString()+" pesos"
                Bcolor.setBackgroundColor(Color.parseColor(i.Color))
                for (j in arrayT!!){
                    val list = PolyUtil.decode(j.polyline)
                    mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor(i.Color)).width(20f))
                }
                img.setImageBitmap(i.Foto)
            }
        })


    }
}
