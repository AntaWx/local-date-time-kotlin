package com.surya_yasa_antariksa.localdatetime

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.surya_yasa_antariksa.localdatetime.database.DateTimeDatabase
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditDataActivity : AppCompatActivity() {

    private lateinit var database: DateTimeDatabase
    private lateinit var datePicker: Button
    private lateinit var saveButton: Button
    private lateinit var updateName: EditText
    private lateinit var updateKegiatan: EditText
    private lateinit var arrowBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data)

        val actionBar = supportActionBar

        actionBar?.hide()

        datePicker = findViewById(R.id.update_date_picker)
        saveButton = findViewById(R.id.update_simpan)
        updateName = findViewById(R.id.update_nama)
        updateKegiatan = findViewById(R.id.update_kegiatan_column)
        database = DateTimeDatabase.getInstance(applicationContext)
        arrowBack = findViewById(R.id.arrow_back_update)

        val id = intent.getIntExtra("id", -1)
        val nama = intent.getStringExtra("nama")
        val tanggal = intent.getStringExtra("tanggal")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dateFormater = tanggal!!.format(formatter)
        val kegiatan = intent.getStringExtra("kegiatan")

        updateName.setText(nama)
        updateKegiatan.setText(kegiatan)
        datePicker.setText(dateFormater)

        datePicker.setOnClickListener {
            val currentDate = LocalDateTime.now()
            val datePickerDialog = DatePickerDialog(this, { _, dayOfMonth, month, year ->
                val selectedDate = LocalDateTime.of(dayOfMonth, month+1, year, 0, 0)
                val formattedDate = selectedDate.format(formatter)
                datePicker.text = formattedDate
                datePicker.apply {
                    setText(formattedDate)
                    error = null
                }
            }, currentDate.year, currentDate.monthValue-1, currentDate.dayOfMonth)
            datePickerDialog.show()
        }

        datePicker.setTextColor(ContextCompat.getColor(this, R.color.white))

        saveButton.setOnClickListener {
            val name = updateName.text.toString()
            val kegiatan = updateKegiatan.text.toString()
            val tanggal = datePicker.text.toString()

            val dateTimeEntity = DateTimeEntity(id,name,tanggal, kegiatan)
            database.getDateTimeDao().update(dateTimeEntity)
            finish()
        }

        arrowBack.setOnClickListener {
            finish()
        }
    }
}