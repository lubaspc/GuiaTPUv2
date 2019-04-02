package com.ittoluca.lubinpc.guiatpu.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream


class CRUD(context: Context) {
    var helper= SQLiteHerlper(context)
    var colRutas = arrayOf("Id_ruta", "Nombre", "Costo", "Foto", "color","JSon")
    var colTrayecto = arrayOf("Id_ruta", "orden", "Lat0", "Long0", "Lat1", "Long1", "distancia", "Tiempo","Polyline")

    fun consultarRutas():ArrayList<Rutas>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Rutas> = ArrayList()
       var c = db.query("Rutas", colRutas, null, null, null, null, null, null)
        while (c.moveToNext()) {
                Array.add(Rutas(
                    c.getInt(c.getColumnIndexOrThrow(colRutas[0])),
                    c.getString(c.getColumnIndexOrThrow(colRutas[1])),
                    c.getDouble(c.getColumnIndexOrThrow(colRutas[2])),
                    BitmapFactory.decodeStream(ByteArrayInputStream(c.getBlob(c.getColumnIndexOrThrow(colRutas[3])))),
                    c.getString(c.getColumnIndexOrThrow(colRutas[4])),
                    c.getString(c.getColumnIndexOrThrow(colRutas[5]))
                ))
        }
        db.close()
        return Array
    }

    fun JSONxID ():ArrayList<String>{
        var array:ArrayList<String> = ArrayList()
        val db: SQLiteDatabase =helper.readableDatabase
        var c = db.query("Rutas", arrayOf(colRutas[5]), null, null, null, null, null, null)
        while (c.moveToNext()){
           array.add( c.getString(c.getColumnIndexOrThrow(colRutas[5])))
        }
        return array
    }

    fun consultarRutasxID(id:String):ArrayList<Rutas>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Rutas> = ArrayList()

        var c = db.query("Rutas", colRutas, " Id_ruta = ?", arrayOf(id),
                             null, null, null, null)

        while (c.moveToNext()) {
                Array.add(Rutas(
                    c.getInt(c.getColumnIndexOrThrow(colRutas[0])),
                    c.getString(c.getColumnIndexOrThrow(colRutas[1])),
                    c.getDouble(c.getColumnIndexOrThrow(colRutas[2])),
                    BitmapFactory.decodeStream(ByteArrayInputStream(c.getBlob(3))),
                    c.getString(c.getColumnIndexOrThrow(colRutas[4])),
                    c.getString(c.getColumnIndexOrThrow(colRutas[5]))
                ))
        }
        db.close()
        return Array
    }

    fun ConsutaTrayectoxID(id:String):ArrayList<Trayecto>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Trayecto> = ArrayList()
        var c = db.query("Trayecto", colTrayecto, " Id_ruta = ?", arrayOf(id), null, null, null, null)
        while (c.moveToNext()) {
                Array.add(
                    Trayecto(
                    c.getInt(c.getColumnIndexOrThrow(colTrayecto[0])),
                    c.getInt(c.getColumnIndexOrThrow(colTrayecto[1])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[2])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[3])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[4])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[5])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[6])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[7])),
                    c.getString(c.getColumnIndexOrThrow(colTrayecto[8]))
                )
                )
        }
        db.close()
        return Array
    }


    fun ConsutaTrayecto():ArrayList<Trayecto>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Trayecto> = ArrayList()
        var c = db.query("Trayecto", colTrayecto, null, null, null, null, null, null)
        while (c.moveToNext()) {
            Array.add(
                Trayecto(
                    c.getInt(c.getColumnIndexOrThrow(colTrayecto[0])),
                    c.getInt(c.getColumnIndexOrThrow(colTrayecto[1])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[2])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[3])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[4])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[5])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[6])),
                    c.getDouble(c.getColumnIndexOrThrow(colTrayecto[7])),
                    c.getString(c.getColumnIndexOrThrow(colTrayecto[8]))
                )
            )
        }
        db.close()
        return Array
    }



    fun insertarTray(T:Trayecto){
        val db: SQLiteDatabase =helper.writableDatabase
        val values=ContentValues()
        values.put(colTrayecto[0],T.id_ruta)
        values.put(colTrayecto[1],T.Orden)
        values.put(colTrayecto[2],T.Lat0)
        values.put(colTrayecto[3],T.Long0)
        values.put(colTrayecto[4],T.Lat1)
        values.put(colTrayecto[5],T.Long1)
        values.put(colTrayecto[6],T.distancia)
        values.put(colTrayecto[7],T.tiempo)
        values.put(colTrayecto[8],T.polyline)
        db.insert("Trayecto",null,values)
    }

    fun insertarRutas(T:Rutas){
        val db: SQLiteDatabase =helper.writableDatabase
        val values=ContentValues()
        values.put(colRutas[0],T.id_ruta)
        values.put(colRutas[1],T.nombre)
        values.put(colRutas[2],T.costo)
        val baos = ByteArrayOutputStream(20480);
        T.Foto!!.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        var blob = baos.toByteArray()
        values.put(colRutas[3],blob)
        values.put(colRutas[4],T.Color)
        values.put(colRutas[5],T.JSON)
        db.insert("Rutas",null,values)
    }
}