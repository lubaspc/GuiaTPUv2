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
    var colTrayecto = arrayOf("Id_ruta", "orden", "Lat0", "Long0", "distancia", "Tiempo","Polyline")
    var colChoque = arrayOf("Id", "ID1", "ID2", "PLat", "PLong","orden1","orden2")

    fun Destroy(){ helper.onUpgrade(helper.writableDatabase,1,1) }

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

    fun consultarRutasID():ArrayList<Int>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Int> = ArrayList()

        var c = db.query("Rutas", arrayOf(colRutas[0]), null,null,
            null, null, null, null)

        while (c.moveToNext()) {
            Array.add(c.getInt(c.getColumnIndexOrThrow(colRutas[0])))
        }
        db.close()

        return Array
    }

    fun ContarRutas():Int{
        val db: SQLiteDatabase =helper.readableDatabase
        var c = db.query("Rutas", arrayOf(colRutas[0]), null,null,
            null, null, null, null)
        db.close()
        return c.count
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
                    c.getString(c.getColumnIndexOrThrow(colTrayecto[6]))
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
        values.put(colTrayecto[4],T.distancia)
        values.put(colTrayecto[5],T.tiempo)
        values.put(colTrayecto[6],T.polyline)
        db.insert("Trayecto",null,values)
    }

    fun insertarRutas(T:RutasI){
        val db: SQLiteDatabase =helper.writableDatabase
        val values=ContentValues()
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

    fun insertarChoque(T:Choques){
        val db: SQLiteDatabase =helper.writableDatabase
        val values=ContentValues()
        values.put(colChoque[1],T.id1)
        values.put(colChoque[2],T.id2)
        values.put(colChoque[3],T.Lat)
        values.put(colChoque[4],T.Long)
        values.put(colChoque[5],T.orden1)
        values.put(colChoque[6],T.orden2)
        db.insert("Choque",null,values)
    }



    fun ConsutaChoques(id1:Int,id2:Int):ArrayList<Choque>{
        val db: SQLiteDatabase =helper.readableDatabase
        var Array :ArrayList<Choque> = ArrayList()
        var c = db.query("Choque", colChoque, "(ID1 = "+id1+" AND  ID2 = "+id2+") OR (ID1 = "+id2+" AND  ID2 = "+id1+")", null, null, null, null, null)
        while (c.moveToNext()) {
            Array.add(
                Choque(
                    c.getInt(c.getColumnIndexOrThrow(colChoque[0])),
                    c.getInt(c.getColumnIndexOrThrow(colChoque[1])),
                    c.getInt(c.getColumnIndexOrThrow(colChoque[2])),
                    c.getDouble(c.getColumnIndexOrThrow(colChoque[3])),
                    c.getDouble(c.getColumnIndexOrThrow(colChoque[4])),
                    c.getInt(c.getColumnIndexOrThrow(colChoque[5])),
                    c.getInt(c.getColumnIndexOrThrow(colChoque[6]))
                )
            )
        }
        db.close()
        return Array
    }


}