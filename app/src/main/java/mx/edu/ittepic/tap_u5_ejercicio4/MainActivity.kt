package mx.edu.ittepic.tap_u5_ejercicio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val baseRemota = FirebaseFirestore.getInstance()
    val lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarDatosDesdeNube()

        button.setOnClickListener {
            val documento = hashMapOf(
                "NOMBRE" to nombre.text.toString(),
                "TELEFONO" to telefono.text.toString(),
                "EDAD" to edad.text.toString().toInt()
            )
            baseRemota.collection("TAP")
                .add(documento) //Significa insertar
                .addOnSuccessListener {
                    //Si funciono
                    AlertDialog.Builder(this).setTitle("EXITO").setMessage("SE INSERTO CORRECTAMENTE")
                        .setPositiveButton("OK", {d, i ->}).show()

                }
                .addOnFailureListener {
                    //Si no funciono
                    AlertDialog.Builder(this).setTitle("ERROR").setMessage("NO SE PUDO INSERTAR")
                        .setPositiveButton("OK", {d, i ->}).show()
                }
            nombre.setText("")
            telefono.setText("")
            edad.setText("")
        }

    }

    fun cargarDatosDesdeNube(){
        baseRemota.collection("TAP").addSnapshotListener { value, error ->
            if (error!=null){
                AlertDialog.Builder(this).setTitle("ERROR").setMessage("NO SE PUDO RECUPERAR LA DATA")
                    .setPositiveButton("OK", {d, i ->}).show()
                return@addSnapshotListener
            }
            lista.clear()
            for (documento in value!!){
                var cadena = "NOMBRE: "+documento.getString("NOMBRE")+"\nTELEFONO: "+
                        documento.getString("TELEFONO")+"\nEDAD: "+documento.get("EDAD").toString()
                lista.add(cadena)
            }
            listadocumentos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista)
        }
    }

}