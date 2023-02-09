package com.example.roomdattabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.roomdattabase.room.Constant
import com.example.roomdattabase.room.dbsmksa
import com.example.roomdattabase.room.tbSiswa
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private val db by lazy { dbsmksa(this) }
    private var tbsisnis: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        tombolperintah()
        setupView()
        tbsisnis = intent.getIntExtra("intent_nis",0)
        Toast.makeText(this,tbsisnis.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){
        val intentType = intent.getIntExtra("intent_type",0)
        when (intentType){
            Constant.TYPE_CREATE -> {

            }
            Constant.TYPE_READ -> {
                btnsimpan.visibility = View.GONE
                btnupdate.visibility=View.GONE
                etnis.visibility= View.GONE
                tampilsemua()
            }
            Constant.TYPE_UPDATE -> {
                btnsimpan.visibility = View.GONE
                etnis.visibility= View.GONE
                tampilsemua()
            }
        }
    }

    fun tombolperintah(){
        btnsimpan.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.tbsisDao().addtbSiswa(
                    tbSiswa(etnis.text.toString().toInt(),etnama.text.toString(),etkelas.text.toString(),etalamat.text.toString())
                )
                finish()
            }
        }
        btnupdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.tbsisDao().updatetbSiswa(
                    tbSiswa(tbsisnis,etnama.text.toString(),etkelas.text.toString(),etalamat.text.toString())
                )
                finish()
            }
        }
    }
    fun tampilsemua(){
        tbsisnis = intent.getIntExtra("intent_nis",0)
        CoroutineScope(Dispatchers.IO).launch {
            val siswa = db.tbsisDao().tampilid(tbsisnis).get(0)
            etnama.setText(siswa.nama)
            etkelas.setText(siswa.kelas)
            etalamat.setText(siswa.alamat)
        }
    }
}