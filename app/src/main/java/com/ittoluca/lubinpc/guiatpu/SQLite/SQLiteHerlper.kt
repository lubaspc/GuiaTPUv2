package com.ittoluca.lubinpc.guiatpu.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLiteHerlper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_RUTAS)
        db?.execSQL(CREATE_TABLE_TRAYECTO)
        db?.execSQL(CREATE_TABLE_CHOQUE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_RUTAS)
        db?.execSQL(DROP_TABLE_TRAYECTO)
        db?.execSQL(DROP_TABLE_CHOQUE)
        onCreate(db)
    }

    companion object {
        val CREATE_TABLE_RUTAS="CREATE TABLE IF NOT EXISTS \"Rutas\" (\n" +
                "\t\"Id_ruta\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"Nombre\"\tVARCHAR(30) NOT NULL UNIQUE,\n" +
                "\t\"Costo\"\tDOUBLE(3 , 3) NOT NULL,\n" +
                "\t\"Foto\"\tBLOB,\n" +
                "\t\"color\"\tINTEGER NOT NULL,\n" +
                "\t\"JSon\"\tTEXT NOT NULL );"

        val CREATE_TABLE_CHOQUE="CREATE TABLE IF NOT EXISTS \"Choque\" (\n" +
                "\t\"Id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"ID1\"\tINTEGER NOT NULL,\n" +
                "\t\"ID2\"\tINTEGER NOT NULL,\n" +
                "\t\"PLat\"\tDOUBLE(4 , 100) NOT NULL,\n" +
                "\t\"PLong\"\tDOUBLE(4 , 100) NOT NULL,\n" +
                "\t\"orden1\"\tINTEGER,\n" +
                "\t\"orden2\"\tINTEGER,\n" +
                "\tFOREIGN KEY(\"ID1\") REFERENCES \"Rutas\"(\"Id_ruta\")\n" +
                "\tFOREIGN KEY(\"ID2\") REFERENCES \"Rutas\"(\"Id_ruta\")\n" +
                " );"

        var CREATE_TABLE_TRAYECTO="CREATE TABLE IF NOT EXISTS \"Trayecto\" (\n" +
                "\t\"Id_ruta\"\tINTEGER NOT NULL,\n" +
                "\t\"orden\"\tINTEGER,\n" +
                "\t\"Lat0\"\tDOUBLE(4 , 100) NOT NULL,\n" +
                "\t\"Long0\"\tDOUBLE(4 , 100) NOT NULL,\n" +
                "\t\"distancia\"\tTEXT NOT NULL,\n" +
                "\t\"Tiempo\"\tTEXT NOT NULL,\n" +
                "\t\"Polyline\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"Id_ruta\") REFERENCES \"Rutas\"(\"Id_ruta\")\n" +
                ");"

        var DROP_TABLE_RUTAS="DROP TABLE IF EXISTS Rutas"
        var DROP_TABLE_TRAYECTO="DROP TABLE IF EXISTS Tratecto"
        var DROP_TABLE_CHOQUE="DROP TABLE IF EXISTS Choque"
        private val DATABASE_NAME = "Bases"
        private val DATABASE_VERSION = 1
    }

}