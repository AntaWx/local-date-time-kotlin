package com.surya_yasa_antariksa.localdatetime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.app.DatePickerDialog
import com.surya_yasa_antariksa.localdatetime.database.DateTimeDatabase
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MasukkanDataActivity : AppCompatActivity() {

    private lateinit var masukanNama : EditText
    private lateinit var masukkanKegiatan: EditText
    private lateinit var datePicker: Button
    private lateinit var database: DateTimeDatabase
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masukkan_data)

        masukanNama = findViewById(R.id.masukkan_nama)
        masukkanKegiatan = findViewById(R.id.kegiatan_column)
        datePicker = findViewById(R.id.date_picker)
        saveButton = findViewById(R.id.simpan)
        database = DateTimeDatabase.getInstance(applicationContext)

        // Menambahkan OnClickListener untuk datePicker
        datePicker.setOnClickListener {
            // Mendapatkan tanggal saat ini
            val currentDate = LocalDateTime.now()
            // Membuat DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, dayOfMonth, month, year ->
                // Membuat objek LocalDateTime dari tanggal yang dipilih
                val selectedDate = LocalDateTime.of(dayOfMonth, month+1, year, 0, 0)
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                // Mengubah LocalDateTime menjadi String menggunakan format tertentu
                val formattedDate = selectedDate.format(formatter)
                // Menampilkan tanggal yang dipilih di EditText
                datePicker.text = formattedDate
            }, currentDate.year, currentDate.monthValue-1, currentDate.dayOfMonth)
            // Menampilkan DatePickerDialog
            datePickerDialog.show()

            saveButton.setOnClickListener {
                val nama = masukanNama.text.toString()
                val kegiatan = masukkanKegiatan.text.toString()
                val tanggal = datePicker.text.toString()

                val dataEntity = DateTimeEntity(0, nama, tanggal, kegiatan)

                database.getDateTimeDao().addAll(dataEntity)
                finish()
            }
        }
    }
}
