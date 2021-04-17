package eliu.gonzalez.misnotas

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_agregar_nota.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.jar.Manifest

class AgregarNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)

        btn_guardar.setOnClickListener {
            guardar_nota()
        }

    }

    fun guardar_nota() {
        //Verifica que tengo los permisos
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            //Si  no los tiene, lo pide al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235
            )
            //Si tiene permisos, procede a guardar
        } else {
            guardar()
            var intent = Intent(this,MainActivity::class.java)
            startActivityForResult(intent,123)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            235 -> {
                //Pregunta si el usuario aceptó los permisos
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    guardar()
                } else {
                    //Si no aceptó, coloca un mensaje
                    Toast.makeText(this, "ERROR: permisos denegados", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun guardar() {
        var titulo = et_titulo.text.toString()
        var cuerpo = et_contenido.text.toString()
        if (titulo == "" && cuerpo == "") {
            Toast.makeText(this, "ERROR: campos vacios", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val archivo: File = File(ubicacion(), titulo + "txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(
                    this,
                    "Se guardo el archivo en la carpeta pública",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: no se guardó el archivo", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun ubicacion(): String {
        val carpeta = File(getExternalFilesDir(null),"notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }

}