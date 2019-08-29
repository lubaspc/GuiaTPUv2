package com.ittoluca.lubinpc.guiatpu

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions


class Mapa(mMap:GoogleMap,context:Context) {
    var mMap:GoogleMap?=null
    var context:Context?=null
    val Toluca= LatLngBounds(LatLng(19.258129,-99.682304), LatLng(19.314109, -99.580785))

    init {
        this.mMap=mMap
        this.context=context
    }

    @SuppressLint("MissingPermission")
    fun Iniialicacion():GoogleMap{
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.uiSettings.isMapToolbarEnabled = true
        mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(19.272689, -99.652044)))
        mMap!!.setMinZoomPreference(15f)
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mMap!!.setLatLngBoundsForCameraTarget(Toluca)
        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.maps_style))
        return mMap!!
    }
}