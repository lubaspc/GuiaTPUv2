package com.ittoluca.lubinpc.guiatpu.Fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.ittoluca.lubinpc.guiatpu.AdaptadorCustom
import com.ittoluca.lubinpc.guiatpu.Mapa
import com.ittoluca.lubinpc.guiatpu.R
import com.ittoluca.lubinpc.guiatpu.SQLite.CRUD
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas
import com.ittoluca.lubinpc.guiatpu.SQLite.Trayecto
import kotlinx.android.synthetic.main.fragment_todas.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Todas : Fragment() {
    var vieww:View?=null
    private lateinit var mMap: GoogleMap
    //                                    Sur-Oeste                     Nor-Este
    val Toluca= LatLngBounds(LatLng(19.258129,-99.682304), LatLng(19.314109, -99.580785))
    var arreglo:ArrayList<Rutas>?=null
    var arregloT:ArrayList<Trayecto>?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vieww=inflater.inflate(R.layout.fragment_todas, container, false)
        var modeldialog= AlertDialog.Builder(vieww!!.context)
        val Dialogvista=layoutInflater.inflate(R.layout.catalogo,null)
        modeldialog.setView(Dialogvista)
        var dialogo=modeldialog.create()
        var list= Dialogvista.findViewById<ListView>(R.id.list)
        arreglo= CRUD(vieww!!.context).consultarRutas()
        val adaptador= AdaptadorCustom(vieww!!.context, arreglo!!)
        list.adapter=adaptador

        var maps=vieww!!.findViewById<MapView>(R.id.maps2)
        if(maps!=null){
            maps.onCreate(null)
            maps.onResume()
            maps.getMapAsync {
                mMap= Mapa(it, vieww!!.context).Iniialicacion()
                for (i in arreglo!!){
                    arregloT= CRUD(vieww!!.context).ConsutaTrayectoxID(i.id_ruta.toString())
                    val list= arrayListOf<LatLng>()
                   for (j in arregloT!!){
                       list.add(LatLng(j.Lat0!!,j.Long0!!))
                   }
                    mMap!!.addPolyline(PolylineOptions().addAll(list).color(Color.parseColor(i.Color)).width(20f))
                }

                mMap.setOnMapClickListener {
                    dialogo.show()
                }


            }

        }
        return vieww
    }
}
