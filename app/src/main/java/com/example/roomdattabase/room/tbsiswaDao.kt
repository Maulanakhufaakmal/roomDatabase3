package com.example.roomdattabase.room

import androidx.room.*

@Dao
interface tbsiswaDao {

    @Insert
    fun addtbSiswa(tbsis :tbSiswa)

    @Update
    fun updatetbSiswa(tbsis: tbSiswa)

    @Delete
    fun deletetbSiswa(tbsis: tbSiswa)

    @Query("SELECT * FROM tbSiswa")
    suspend fun tampilsemua():List<tbSiswa>

    @Query("SELECT * FROM tbSiswa WHERE nis=:siswa_nis")
    fun tampilid(siswa_nis: Int) : List<tbSiswa>

}