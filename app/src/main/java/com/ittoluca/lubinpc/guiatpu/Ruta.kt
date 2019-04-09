package com.ittoluca.lubinpc.guiatpu

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class Ruta : AppCompatActivity(){

    private lateinit var mMap: GoogleMap
    var Destino: LatLng= LatLng(0.0,0.0)
    var Origen: LatLng= LatLng(0.0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruta)

        Origen = LatLng(intent.getDoubleExtra("Lat0", 0.0), intent.getDoubleExtra("Long0", 0.0))
        Destino = LatLng(intent.getDoubleExtra("Lat1", 0.0), intent.getDoubleExtra("Long1", 0.0))

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapR) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            mMap = Mapa(it, this).Iniialicacion()
            calculos()
        })
    }

    private fun calculos() {
        var r = 5000.0
        var Ruta: Rutas? = null
        var lg: LatLng? = null
        var orden = 0
        var r2 = 5000.0
        var Ruta2: Rutas? = null
        var lg2: LatLng? = null
        var orden2 = 0
        val arrayR = CRUD(this).consultarRutas()
        for (R in arrayR) {
            val arrayT = CRUD(this).ConsutaTrayectoxID(R.id_ruta.toString())
            for (T in arrayT) {
                var list = PolyUtil.decode(T.polyline!!)
                for (l in list) {
                    val distancia = Math.sqrt(Math.pow(l.latitude - Origen.latitude, 2.0) + Math.pow(l.longitude - Origen.longitude, 2.0))
                    if (distancia < r) {
                        r = distancia
                        Ruta = R
                        lg = l
                        orden = T.Orden!!
                    }
                    val distancia2 = Math.sqrt(Math.pow(l.latitude - Destino.latitude, 2.0) + Math.pow(l.longitude - Destino.longitude, 2.0))
                    if (distancia2 < r2) {
                        r2 = distancia2
                        Ruta2 = R
                        lg2 = l
                        orden2 = T.Orden!!
                    }
                }
            }

        }

        consulta(Origen,lg!!)
        consulta(Destino,lg2!!)
        var arregloT= CRUD(this).ConsutaTrayectoxID(Ruta!!.id_ruta.toString())
        for (j in arregloT!!){
            val list = PolyUtil.decode(j.polyline)
            mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor(Ruta!!.Color)).width(20f))
        }
        arregloT= CRUD(this).ConsutaTrayectoxID(Ruta2!!.id_ruta.toString())
        for (j in arregloT!!){
            val list = PolyUtil.decode(j.polyline)
            mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor(Ruta2!!.Color)).width(20f))
        }



    }
     fun consulta(O:LatLng,D:LatLng){
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    O!!.latitude + "," + O!!.longitude + "&destination=" +
                    D!!.latitude + "," +D!!.longitude +"&mode=walking"+ "&key=" +getString(R.string.google_maps_key)
        val queue = Volley.newRequestQueue(this)
        val solisitud = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            try {
                trazarRuta(JSONObject(it), mMap)
                Log.d("","")
            } catch (e: Exception) { }
        }, Response.ErrorListener {
            val response = it.networkResponse;
            if (response != null && response.data != null) {
                val errorString = String(response.data);
                Log.i("log error", errorString);
            }
        })

        queue.add(solisitud)
    }

    fun trazarRuta(jso: JSONObject,mMap:GoogleMap) {
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jSteps: JSONArray

        try {
            jRoutes = jso!!.getJSONArray("routes")
            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                for (j in 0 until jLegs.length()) {
                    jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")
                    for (k in 0 until jSteps.length()) {
                        val polyline = "" + ((jSteps.get(k) as JSONObject).get("polyline") as JSONObject).get("points")
                        val list = PolyUtil.decode(polyline)
                        mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor("#FFFFFF")).width(15f).pattern(arrayListOf(
                            Dot(), Gap(10f)
                        )))
                    }
                }
            }
        } catch (e: JSONException) {
            Log.d("Esntramos",e.toString())
        }
    }

    fun distancia (O:LatLng,D:LatLng):Double{
        var Distancia=0.0
        Distancia=Math.sqrt(Math.pow(O.latitude - D.latitude, 2.0) + Math.pow(O.longitude - D.longitude, 2.0))
        return Distancia
    }
        
    }

