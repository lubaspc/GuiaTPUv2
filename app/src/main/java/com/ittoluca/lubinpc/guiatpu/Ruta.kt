package com.ittoluca.lubinpc.guiatpu

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import com.ittoluca.lubinpc.guiatpu.SQLite.PuntosCortos
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas
import com.ittoluca.lubinpc.guiatpu.SQLite.Trayecto
import kotlinx.android.synthetic.main.activity_ruta.*
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
            mMap.addMarker(MarkerOptions().title("Inicio").position(Origen).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            mMap.addMarker(MarkerOptions().position(Destino).title("Destino").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
            var o =calculos()
            var modeldialog= AlertDialog.Builder(this)
            val Dialogvista=layoutInflater.inflate(R.layout.informacion,null)
            modeldialog.setView(Dialogvista)
            var dialogo=modeldialog.create()
            FButton.setOnClickListener {
                dialogo.show()
            }

            var modeldialog2= AlertDialog.Builder(this)
            val Dialogvista2=layoutInflater.inflate(R.layout.instrucciones,null)
            modeldialog2.setView(Dialogvista2)
            var dialogo2=modeldialog2.create()
            FBotInf.setOnClickListener {
                dialogo2.show()
            }
        })

    }

    private fun calculos():Rutas {
        val crud = CRUD(this)
        val arrayR = crud.consultarRutasID()
        var PC= arrayListOf<PuntosCortos>()
        arrayR.forEach {
            val aT=crud.ConsutaTrayectoxID(it.toString())
            var distancia=5000000.0
            for (T in aT){
               val d= dis(Origen,LatLng(T.Lat0!!,T.Long0!!))
                if (d<distancia){
                    distancia=d
                    PC.add(PuntosCortos(it!!,T.Orden!!,Origen,LatLng(T.Lat0!!,T.Long0!!),d))
                }
            }
        }
        PC.removeAt(0)
       while (PC.size>1){
           if (PC[0].Orden>PC[1].Orden)
               PC.removeAt(0)
           else
               PC.removeAt(1)
       }
        val T=crud.ConsutaTrayectoxID(PC[0].id_ruta.toString())
        var PC2= arrayListOf<PuntosCortos>()
            var distancia=5000000.0
            for (t in T){
                val d= dis(Destino,LatLng(t.Lat0!!,t.Long0!!))
                if (d<distancia){
                    distancia=d
                    PC2.add(PuntosCortos(t.id_ruta!!,t.Orden!!,Destino,LatLng(t.Lat0!!,t.Long0!!),d))
                }
            }
        while (PC2.size>1){
            if (PC2[0].Orden<PC2[1].Orden)
                PC2.removeAt(0)
            else
                PC2.removeAt(1)
        }
        val list= arrayListOf<LatLng>()
        for (t in T){
            if(t.Orden!!>=PC[0].Orden && t.Orden!!<=PC2[0].Orden){
                list.add(LatLng(t.Lat0!!,t.Long0!!))
            }
        }
        var o=crud.consultarRutasxID(PC[0].id_ruta.toString())
        mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor(o[0].Color)).width(25f))
        consulta(Origen,PC[0].PuntoF)
        consulta(Destino,PC2[0].PuntoF)
        mMap.addMarker(MarkerOptions().position(PC[0].PuntoF).title("Punto De aborde").icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
        mMap.addMarker(MarkerOptions().position(PC2[0].PuntoF).title("Punto de Desenso").icon(BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        return o[0]


    }

    //O= es el punto de inicio D: es el punto de destino
     fun consulta(O:LatLng,D:LatLng){
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                    O!!.latitude + "," + O!!.longitude + "&destination=" +
                    D!!.latitude + "," +D!!.longitude +"&mode=walking"+ "&key=" +getString(R.string.google_maps_key)
        val queue = Volley.newRequestQueue(this)
        val solisitud = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            try {
                trazarRuta(JSONObject(it), mMap)
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
                        mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor("#FFFFFF")).width(20f).pattern(arrayListOf(
                            Dot(), Gap(10f)
                        )))
                    }
                }
            }
        } catch (e: JSONException) {
            Log.d("Esntramos",e.toString())
        }
    }

    fun dis (O:LatLng,D:LatLng):Double{
        return Math.sqrt(Math.pow(O.latitude - D.latitude, 2.0) + Math.pow(O.longitude - D.longitude, 2.0))
    }
        
    }

