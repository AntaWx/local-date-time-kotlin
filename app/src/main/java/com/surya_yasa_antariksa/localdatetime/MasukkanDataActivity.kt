package com.surya_yasa_antariksa.localdatetime

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.*
import com.surya_yasa_antariksa.localdatetime.database.DateTimeDatabase
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity
import com.surya_yasa_antariksa.localdatetime.databinding.ActivityMasukkanDataBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MasukkanDataActivity : AppCompatActivity() {

    private lateinit var masukanNama : EditText
    private lateinit var masukkanKegiatan: EditText
    private lateinit var datePicker: Button
    private lateinit var database: DateTimeDatabase
    private lateinit var saveButton: Button
    private lateinit var binding: ActivityMasukkanDataBinding
    private lateinit var arrowBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasukkanDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        masukanNama = findViewById(R.id.masukkan_nama)
        masukkanKegiatan = findViewById(R.id.kegiatan_column)
        datePicker = findViewById(R.id.date_picker)
        saveButton = findViewById(R.id.simpan)
        database = DateTimeDatabase.getInstance(applicationContext)
        arrowBack = findViewById(R.id.arrow_back)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val actionBar = supportActionBar

        actionBar?.hide()

        // Menambahkan OnClickListener untuk datePicker
        datePicker.setOnClickListener {
            // Mendapatkan tanggal saat ini
            val currentDate = LocalDateTime.now()
            // Membuat DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, dayOfMonth, month, year ->
                // Membuat objek LocalDateTime dari tanggal yang dipilih
                val selectedDate = LocalDateTime.of(dayOfMonth, month+1, year, 0, 0)
                // Mengubah LocalDateTime menjadi String menggunakan format tertentu
                val formattedDate = selectedDate.format(formatter)
                // Menampilkan tanggal yang dipilih di EditText
                datePicker.text = formattedDate
                // Menampilkan tanggal yang dipilih di EditText dan menghapus tanda error
                datePicker.apply {
                    setText(formattedDate)
                    error = null
                }
            }, currentDate.year, currentDate.monthValue-1, currentDate.dayOfMonth)
            // Menampilkan DatePickerDialog
            datePickerDialog.show()
        }

        saveButton.setOnClickListener {
            val nama = masukanNama.text.toString()
            val kegiatan = masukkanKegiatan.text.toString()
            val tanggal = datePicker.text.toString()

            when{
                nama.isEmpty() ->{
                    binding.masukkanNama.error = "Nama Harus Diisi"
                    binding.masukkanNama.requestFocus()
                    return@setOnClickListener
                }
                kegiatan.isEmpty() ->{
                    binding.kegiatanColumn.error = "Kegiatan harus diisi"
                    binding.kegiatanColumn.requestFocus()
                    return@setOnClickListener
                }
                tanggal.isEmpty() ->{
                    binding.datePicker.error = "tanggal harus diisi"
                    binding.datePicker.requestFocus()
                    return@setOnClickListener
                }
            }
            val dataEntity = DateTimeEntity(0, nama, tanggal, kegiatan)

            database.getDateTimeDao().addAll(dataEntity)
            Toast.makeText(this, "data berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }

        arrowBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
