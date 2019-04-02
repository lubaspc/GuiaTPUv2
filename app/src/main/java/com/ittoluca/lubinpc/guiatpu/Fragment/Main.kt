package com.ittoluca.lubinpc.guiatpu.Fragment


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ittoluca.lubinpc.guiatpu.Mapa
import com.ittoluca.lubinpc.guiatpu.R
import com.ittoluca.lubinpc.guiatpu.Ruta


class Main : Fragment(){

    private lateinit var mMap: GoogleMap
    var vieww:View?=null
    var fuseLocationLine: FusedLocationProviderClient? = null
    var locationRequest: LocationRequest? = null

    var callback: LocationCallback? = null
    //                                    Sur-Oeste                     Nor-Este
    val Toluca= LatLngBounds(LatLng(19.25,-99.682304), LatLng(19.314109, -99.57))
    var Destino: LatLng?=null
    var Origen: LatLng?=null
    var OriDes=true
    var Actualisa=true
    var Offline=true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_main, container, false)
        vieww=view

        fuseLocationLine = LocationServices.getFusedLocationProviderClient(view.context);
        inicalicacionLocation()

        var maps=view.findViewById<MapView>(R.id.maps)
        if(maps!=null){
            maps.onCreate(null)
            maps.onResume()
            maps.getMapAsync {
                inicial(it)
                ObtenetUbicacion()
            }
        }

        callback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                for (ubicacion in p0?.locations!!) {
                    if (Actualisa) {
                        Origen = LatLng(ubicacion.latitude, ubicacion.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(Origen))
                        if (dentrode(ubicacion!!)) {
                            Log.d("Margen", "Estoy dentro ")
                            var modeldialog = AlertDialog.Builder(view.context)
                            modeldialog.setPositiveButton("", DialogInterface.OnClickListener { dialog, which ->
                                val tv = view.findViewById<TextView>(R.id.TVBuscar)
                                Toast.makeText(view.context, "Si entre", Toast.LENGTH_LONG).show()
                                tv.setText("Selecciona el origen")
                                OriDes = false
                                Offline = false

                            })
                            modeldialog.setNegativeButton("", DialogInterface.OnClickListener { dialog, which ->

                            })
                            modeldialog.setPositiveButtonIcon(resources.getDrawable(R.drawable.palomita))
                            modeldialog.setNegativeButtonIcon(resources.getDrawable(R.drawable.tache))
                            val Dialogvista = layoutInflater.inflate(R.layout.notfueraarea, null)
                            modeldialog.setView(Dialogvista)
                            var dialogo = modeldialog.create()
                            dialogo.setCancelable(false)
                            dialogo.setCanceledOnTouchOutside(false)
                            dialogo.show()

                        }
                        Actualisa = false
                    }

                }
            }
        }

        return view
    }

    fun dentrode(Origen: Location):Boolean{
        val b=  Origen.latitude>=Toluca.southwest.latitude  &&
                Origen.latitude<=Toluca.northeast.latitude  &&
                Origen.longitude>=Toluca.southwest.longitude&&
                Origen.longitude<=Toluca.northeast.longitude
        return !b
    }

         fun inicial(googleMap: GoogleMap) {
        mMap = Mapa(googleMap, vieww!!.context).Iniialicacion()
        mMap.setOnMapClickListener {
            var OD:String?
            if(OriDes) {
                Destino = it
                OD="Destino"
            }else{
                Origen =it
                OD="Origen"
            }
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title(OD))
            // mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            if(Offline) {
                alertDialog()
            }else{
                val tv=vieww!!.findViewById<TextView>(R.id.TVBuscar)
                tv.setText("Selecciona tu destino")
                OriDes=true
                Offline=true
            }
        }
    }

    fun alertDialog(){
        var modeldialog= AlertDialog.Builder(vieww!!.context)
        val Dialogvista=layoutInflater.inflate(R.layout.confirmacion,null)
        modeldialog.setPositiveButton("",DialogInterface.OnClickListener { dialog, which ->
            val intent= Intent(vieww!!.context, Ruta::class.java)
            intent.putExtra("Lat0",Origen?.latitude)
            intent.putExtra("Long0",Origen?.longitude)
            intent.putExtra("Lat1",Destino?.latitude)
            intent.putExtra("Long1",Destino?.longitude)
            startActivity(intent)
        })
        modeldialog.setPositiveButtonIcon(resources.getDrawable(R.drawable.palomita))
        modeldialog.setView(Dialogvista)
        var dialogo=modeldialog.create()
        dialogo.window.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
        dialogo.setCancelable(false)
        dialogo.setCanceledOnTouchOutside(false)
        val Borigen=Dialogvista.findViewById<Button>(R.id.Borigen)
        val Bdestino=Dialogvista.findViewById<Button>(R.id.BDestino)
        Borigen.setOnClickListener {
            dialogo.cancel()
            val tv = vieww!!.findViewById<TextView>(R.id.TVBuscar)
            tv.text="Selecciona el Origen"
            OriDes=false
        }
        Bdestino.setOnClickListener {
            val tv = vieww!!.findViewById<TextView>(R.id.TVBuscar)
            tv.text="Selecciona el Destino"
            dialogo.cancel()
            OriDes=true
        }
        dialogo.show()
    }

    @SuppressLint("RestrictedApi")
    private fun inicalicacionLocation() {
        locationRequest= LocationRequest()
        locationRequest?.interval=100
        locationRequest?.fastestInterval=50
        locationRequest?.priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

    }

    @SuppressLint("MissingPermission")
    private fun ObtenetUbicacion() { fuseLocationLine?.requestLocationUpdates(locationRequest,callback,null) }

    override fun onPause() { super.onPause(); fuseLocationLine?.removeLocationUpdates(callback) }


}
