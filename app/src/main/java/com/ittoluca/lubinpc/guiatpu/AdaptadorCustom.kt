package com.ittoluca.lubinpc.guiatpu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.ittoluca.lubinpc.guiatpu.SQLite.Rutas

class AdaptadorCustom(var context:Context,item:ArrayList<Rutas>):BaseAdapter() {

    var item:ArrayList<Rutas>?=null
    init {
        this.item=item
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder:ViewHolder?=null
        var vista:View?=convertView
        if (vista==null){
            vista=LayoutInflater.from(context).inflate(R.layout.item,null)
            holder=ViewHolder(vista)
            vista.tag=holder
        }else{
            holder=vista.tag as? ViewHolder
        }
        var items=getItem(position)as Rutas
        holder?.Bcolo!!.setBackgroundColor(Color.parseColor(items.Color))
        holder?.TNom!!.text=items.nombre
        holder?.Bcolo!!.setOnClickListener {
            val intent=Intent(context,RutasInf::class.java)
            intent.putExtra("id",items!!.id_ruta.toString())
            startActivity(context,intent,null)
        }
        holder?.TNom!!.setOnClickListener {
            val intent=Intent(context,RutasInf::class.java)
            intent.putExtra("id",items!!.id_ruta.toString())
            startActivity(context,intent,null)
        }
        return vista!!
    }

    override fun getItem(position: Int): Any {
        return item?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return item!!.count()
    }
    private class ViewHolder(vista:View){
        var Bcolo:Button?=null
        var TNom:TextView?=null

        init {
            Bcolo=vista.findViewById(R.id.BColo)
            TNom=vista.findViewById(R.id.TVNombre)
        }

    }
}
