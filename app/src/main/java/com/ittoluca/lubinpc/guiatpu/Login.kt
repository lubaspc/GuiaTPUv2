package com.ittoluca.lubinpc.guiatpu

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas
import com.ittoluca.lubinpc.guiatpu.SQLite.RutasI
import com.ittoluca.lubinpc.guiatpu.SQLite.Trayecto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class Login : AppCompatActivity() {
    val c=CRUD(this)
    private val PermisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {

        fullscreen_content.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        supportActionBar?.show()
    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true


        Login_continuar.setOnClickListener {
            if(Terminos.isChecked){
                if(!ValidarPermisos()) {
                    requestPermissions(arrayOf(PermisoFineLocation),1)
                }
                val prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("bandera",true)
                editor.commit()
                var modeldialog= AlertDialog.Builder(this)
                val Dialogvista=layoutInflater.inflate(R.layout.cargadno,null)
                modeldialog.setView(Dialogvista)

                var dialogo=modeldialog.create()
                dialogo.setCanceledOnTouchOutside(false)
                dialogo.setCancelable(false)
                dialogo.window.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.Transpate)))
                dialogo.show()


                GlobalScope.launch {
                    insertarValores()
                    dialogo.cancel()
                    finish()
                }
           }
        }

    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun hide() {
        supportActionBar?.hide()
        mVisible = false

        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }


    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {private val UI_ANIMATION_DELAY = 300 }

    private fun ValidarPermisos(): Boolean {
        return ActivityCompat.checkSelfPermission(this, PermisoFineLocation) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1-> {
                var permiso=true
                for(i in grantResults)
                    permiso=(i==PackageManager.PERMISSION_GRANTED)&&permiso
                if(grantResults.size>0&& permiso){

                }
                else {
                    Toast.makeText(this, "Es nesesrio que des permiso", Toast.LENGTH_LONG).show()
                    finishAffinity()
                }
            }
        }
    }

    fun insertarValores(){
        var Arrat:ArrayList<RutasI> = ArrayList()
        var bit= BitmapFactory.decodeResource(this.resources, R.drawable.tecmonterrey)
        var read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonrana))).readText()
        Arrat.add(RutasI("San Jose La pilita - Tec Monterrey",10.0,bit,"#80FF00",read))

        /*2*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.centro)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsoncosmo))).readText()
        Arrat.add(RutasI("San Jose La pilita - Cosmovitral",10.0,bit,"#FF0000",read))

        /*3*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.maquinita)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonmaquinita))).readText()
        Arrat.add(RutasI("San Jose La pilita - La maquinita",10.0,bit,"#470000",read))

        /*4*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.fabela)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonisidro))).readText()
        Arrat.add(RutasI("San Jose La pilita - Isidro Fabela",10.0,bit,"#E68100",read))

        /*5*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.cu)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonconfcentro))).readText()
        Arrat.add(RutasI("San Jose La pilita - Centro -CU",10.0,bit,"#FB5D5D",read))


        c.Destroy()
        for( i in Arrat)
            c.insertarRutas(i)
        insertarTrayecto(c.consultarRutas())
    }

    fun insertarTrayecto(Arrat:ArrayList<Rutas>){
        var array:ArrayList<Trayecto> = ArrayList()
        for (p in Arrat){
            val jRoutes: JSONArray
            var jLegs: JSONArray
            var jSteps: JSONArray
            var json=JSONObject(p.JSON)
            try {
                var x=0
                jRoutes = json.getJSONArray("routes")
                for (i in 0 until jRoutes.length()) {
                    jLegs = (jRoutes.get(i) as JSONObject).getJSONArray("legs")
                    for (j in 0 until jLegs.length()) {
                        jSteps = (jLegs.get(j) as JSONObject).getJSONArray("steps")
                        for (k in 0 until jSteps.length()) {
                            x++
                            val polyline = "" + ((jSteps.get(k) as JSONObject).get("polyline") as JSONObject).get("points")
                            val distancia = "" + ((jSteps.get(k) as JSONObject).get("distance") as JSONObject).get("text")
                            var dis=""
                            for (d in distancia){
                                if (d==' ')
                                    break
                                dis+=d
                            }
                            val duracion = "" + ((jSteps.get(k) as JSONObject).get("duration") as JSONObject).get("text")
                            var t=""
                            for (d in duracion){
                                if (d==' ')
                                    break
                                t+=d
                            }
                            val lat0 = "" + ((jSteps.get(k) as JSONObject).get("start_location") as JSONObject).get("lat")
                            val lng0 = "" + ((jSteps.get(k) as JSONObject).get("start_location") as JSONObject).get("lng")
                            val lat1 = "" + ((jSteps.get(k) as JSONObject).get("end_location") as JSONObject).get("lat")
                            val lng1 = "" + ((jSteps.get(k) as JSONObject).get("end_location") as JSONObject).get("lng")
                            array.add(Trayecto(p.id_ruta!!,x,lat0.toDouble(),lng0.toDouble(),dis.toDouble(),t.toDouble(),polyline))
                        }
                    }
                }
            } catch (e: JSONException) {
                Log.d("Esntramos",e.toString())
            }

        }
        val c= CRUD(this)
        for (i in array)
            c.insertarTray(i)
    }



    fun consultaP(url:String,T:Int){
        val queue = Volley.newRequestQueue(this)
        val solisitud = StringRequest(Request.Method.GET, url, Response.Listener<String> {

            obtenerLinks(JSONObject(it),T)

        }, Response.ErrorListener {
            val response = it.networkResponse;
            if (response != null && response.data != null) {
                val errorString = String(response.data);

            }
        })

        queue.add(solisitud)
    }

    private fun obtenerLinks(Json: JSONObject,T:Int) {
        when(T){
            1->{
                var links=Json.getJSONArray("links")
                for (l in 0 until links.length()){
                    var lk =links[l] as JSONObject
                    GlobalScope.async {  consultaP( lk.getString("link"),2)}
                }
            }
            2->GlobalScope.async {rutas(Json)}
        }
    }

    private fun rutas(json: JSONObject) {
        var name=json.getString("name")
        var costo=json.getString("costo")
        var linkImg =json.getString("nameimg")
        var color =json.getString("color")
        var image: Bitmap?=null


        image = Picasso.with(this@Login).load(linkImg).get()
        c.insertarRutas(RutasI(name,costo.toDouble(),image!!,color,json.toString()))


    }

}
