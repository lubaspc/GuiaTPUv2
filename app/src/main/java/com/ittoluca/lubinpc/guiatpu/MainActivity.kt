package com.ittoluca.lubinpc.guiatpu

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ittoluca.lubinpc.guiatpu.Fragment.Acercad
import com.ittoluca.lubinpc.guiatpu.Fragment.Main
import com.ittoluca.lubinpc.guiatpu.Fragment.Todas
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ruta.*

class MainActivity : AppCompatActivity() {

    private val PermisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
               supportFragmentManager.beginTransaction().replace(R.id.contenedor, Main()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportFragmentManager.beginTransaction().replace(R.id.contenedor, Todas()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        val bandActivity = prefs.getBoolean("bandera", false)
        if (!bandActivity){
            val intento = Intent(this,Login::class.java)
            startActivity(intento)
        }else{
            if(!ValidarPermisos()) {
                requestPermissions(arrayOf(PermisoFineLocation),1)
            }else{
                supportFragmentManager.beginTransaction().replace(R.id.contenedor, Main()).commit()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                var modeldialog= AlertDialog.Builder(this)
                val Dialogvista=layoutInflater.inflate(R.layout.acercami,null)
                modeldialog.setView(Dialogvista)
                var dialogo=modeldialog.create()
                    dialogo.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun ValidarPermisos(): Boolean {
        return ActivityCompat.checkSelfPermission(this, PermisoFineLocation) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1-> {
                var permiso=true
                for(i in grantResults)
                    permiso=(i== PackageManager.PERMISSION_GRANTED)&&permiso
                if(grantResults.size>0&& permiso){

                }
                else {
                    Toast.makeText(this, "Es nesesrio que des permiso", Toast.LENGTH_LONG).show()
                    finishAffinity()
                }
            }
        }
    }

}
