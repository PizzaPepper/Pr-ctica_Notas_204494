package eliu.gonzalez.misnotas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.nota_layout.view.*
import java.io.File

class AdaptadorNotas: BaseAdapter {
    var context: Context
    var notas = ArrayList<Nota>()

    constructor(context: Context,notas:ArrayList<Nota>){
        this.context=context
        this.notas=notas
    }

    override fun getCount(): Int {
        return notas.size
    }

    override fun getItem(position: Int): Any {
        return notas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflator = LayoutInflater.from(context)
        var vista = inflator.inflate(R.layout.nota_layout,null)
        var nota = notas[position]

        vista.tv_titulo_det.text = nota.titulo
        vista.tv_contenido_det.text = nota.contenido

        vista.btn_borrar.setOnClickListener {
            eliminar(nota.titulo)
            notas.remove(nota)
            this.notifyDataSetChanged()
        }

        return vista
    }

    private fun eliminar(titulo: String){
        if(titulo == ""){
            Toast.makeText(context,"Error:  título vacío",Toast.LENGTH_SHORT).show()
        }else{
            try {
                val archivo = File(ubicacion(),titulo+".txt")
                archivo.delete()

                Toast.makeText(context,"Se eliminó el archivo",Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(context,"Error: no se pudo eliminar el archivo",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ubicacion(): String {
        val carpeta = File(context?.getExternalFilesDir(null),"notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }

}