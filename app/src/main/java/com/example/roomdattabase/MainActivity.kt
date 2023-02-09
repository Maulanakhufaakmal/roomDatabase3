package com.example.roomdattabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdattabase.room.Constant
import com.example.roomdattabase.room.dbsmksa
import com.example.roomdattabase.room.tbSiswa
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val db by lazy { dbsmksa(this) }
    lateinit var siswaAdapter: SiswaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        halEdit()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadData()

    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val siswa = db.tbsisDao().tampilsemua()
            Log.d("MainActivity","dbResponse: $siswa")
            withContext(Dispatchers.Main){
                siswaAdapter.setData(siswa)
            }
        }
    }

    private fun halEdit(){
        btnInput.setOnClickListener {
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(tbsisnis:Int, intentType:Int){
        startActivity(Intent(applicationContext,EditActivity::class.java)
            .putExtra("intent_nis",tbsisnis)
            .putExtra("intent_type",intentType)
        )
    }

    fun setupRecyclerView(){
        siswaAdapter = SiswaAdapter(arrayListOf(),object : SiswaAdapter.onAdapterListener{
            override fun onClick(tbsis: tbSiswa) {
                intentEdit(tbsis.nis,Constant.TYPE_READ)
            }

            override fun onUpdate(tbsis: tbSiswa) {
                intentEdit(tbsis.nis,Constant.TYPE_UPDATE)
            }

            override fun onDelete(tbsis: tbSiswa) {

                deleteAlert(tbsis)
            }
        })
        //id recyclerview
        Listdatasiswa.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = siswaAdapter
        }
    }
    private fun deleteAlert(tbsis: tbSiswa){
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Konfirmasi Hapus")
            setMessage("Yakin hapus ${tbsis.nama}?")
            setNegativeButton("Batal"){dialogInterface,i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){dialogInterface,i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.tbsisDao().deletetbSiswa(tbsis)
                    dialogInterface.dismiss()
                    loadData()
                }
            }
        }
        dialog.show()
    }
}