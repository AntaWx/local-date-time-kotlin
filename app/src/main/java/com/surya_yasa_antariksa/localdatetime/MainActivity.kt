package com.surya_yasa_antariksa.localdatetime

    import android.annotation.SuppressLint
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import androidx.appcompat.app.AlertDialog
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.RecyclerView.VERTICAL
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.surya_yasa_antariksa.localdatetime.database.DateTimeDatabase
    import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity

    class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

        private lateinit var recyclerView: RecyclerView
        private lateinit var addButton: FloatingActionButton
        private var list = mutableListOf<DateTimeEntity>()
        private lateinit var adapter: UserAdapter
        private lateinit var database: DateTimeDatabase

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            recyclerView = findViewById(R.id.recycle_view)
            addButton = findViewById(R.id.add_button)

            database = DateTimeDatabase.getInstance(applicationContext)
            adapter = UserAdapter(list, this)

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)

            addButton.setOnClickListener{
                startActivity(Intent(this, MasukkanDataActivity::class.java))
            }
        }

        override fun onResume() {
            super.onResume()
            getData()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun getData(){
            list.clear()
            list.addAll(database.getDateTimeDao().getAll())
            adapter.notifyDataSetChanged()
        }

        override fun onUpdateClick(position: Int) {
            // Kode yang akan dieksekusi saat tombol update di klik
        }

        override fun onDeleteClick(position: Int) {
            val user = list[position]
            AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("Ya") { _, _ ->
                    database.getDateTimeDao().delete(user)
                    getData()
                }
                .setNegativeButton("Tidak", null)
                .create()
                .show()
        }
    }