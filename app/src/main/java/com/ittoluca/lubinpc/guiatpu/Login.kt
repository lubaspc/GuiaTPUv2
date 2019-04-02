package com.ittoluca.lubinpc.guiatpu

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.maps.android.PolyUtil
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas
import com.ittoluca.lubinpc.guiatpu.SQLite.Trayecto
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class Login : AppCompatActivity() {

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
           val prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean("bandera", true)
            editor.commit()
           /* when(getFirstTimeRun()){
                0->insertarValores()
            }*/
            insertarValores()
           finish()
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

    companion object {
        private val UI_ANIMATION_DELAY = 300
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1-> {
                var permiso=true
                for(i in grantResults)
                    permiso=(i== PackageManager.PERMISSION_GRANTED)&&permiso
                if(grantResults.size>0&& permiso)
                else{
                    Toast.makeText(this,"Es nesesario que despermiso para continuar", Toast.LENGTH_LONG).show()
                    finishAffinity() }
            }
        }
    }

    private fun ValidarPermisos(): Boolean {
        return ActivityCompat.checkSelfPermission(this, PermisoFineLocation) == PackageManager.PERMISSION_GRANTED
    }


    override fun onStart() {
        super.onStart()
        if(!ValidarPermisos())
            requestPermissions(arrayOf(PermisoFineLocation),1)

    }
    fun insertarValores(){
        var Arrat:ArrayList<Rutas> = ArrayList()
        var bit= BitmapFactory.decodeResource(this.resources, R.drawable.tecmonterrey)
        var read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonrana1))).readText()
        Arrat.add(Rutas(1,"San Jose La pilita - Tec Monterrey",10.0,bit,"#80FF00",read))

            /*1*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.tecmonterrey)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonrana2))).readText()
        Arrat.add(Rutas(2,"Tec Monterrey - San Jose La pilita",10.0,bit,"#4C8C0C",read))

        /*2*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.centro)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonconfcentro1))).readText()
        Arrat.add(Rutas(3,"San Jose La pilita - Cosmovitral",10.0,bit,"#FF0000",read))

        /*3*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.centro)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonconfcentro2))).readText()
        Arrat.add(Rutas(4,"Cosmovitral - San Jose La pilita",10.0,bit,"#D30000",read))

        /*4*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.maquinita)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonmaquinita1))).readText()
        Arrat.add(Rutas(5,"San Jose La pilita - La maquinita",10.0,bit,"#470000",read))

        /*5*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.maquinita)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jasonmaquinita3))).readText()
        Arrat.add(Rutas(6,"La maquinita - San Jose La pilita",10.0,bit,"#A41313",read))

        /*6*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.fabela)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonisidro1))).readText()
        Arrat.add(Rutas(7,"San Jose La pilita - Isidro Fabela",10.0,bit,"#E68100",read))

        /*7*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.fabela)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonisidro2))).readText()
        Arrat.add(Rutas(8,"Isidro Fabela- San Jose La pilita",10.0,bit,"#7C5804",read))

        /*8*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.cu)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonconfcentro1))).readText()
        Arrat.add(Rutas(9,"San Jose La pilita - Centro -CU",10.0,bit,"#FB5D5D",read))

        /*9*/ bit= BitmapFactory.decodeResource(this.resources, R.drawable.cu)
        read = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.jsonconfcentro2))).readText()
        Arrat.add(Rutas(10,"CU - Centro - San Jose La pilita",10.0,bit,"#491313",read))

        val c=CRUD(this)
        for( i in Arrat)
            c.insertarRutas(i)
        insertarTrayecto(Arrat)
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
                            array.add(Trayecto(p.id_ruta!!,x,lat0.toDouble(),lng0.toDouble(),lat1.toDouble(),lng1.toDouble(),dis.toDouble(),t.toDouble(),polyline))
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

    //Retorna: 0 primera vez / 1 no es primera vez / 2 nueva versión
    fun getFirstTimeRun():Int {
    var sp = this.getSharedPreferences("MYAPP", 0);
    var result =0
    var currentVersionCode = BuildConfig.VERSION_CODE;
    var lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
    if (lastVersionCode == -1)
        result = 0
    else if (lastVersionCode == currentVersionCode)
        result =  1
        else
        result=2

    sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
    return result;
}


}
