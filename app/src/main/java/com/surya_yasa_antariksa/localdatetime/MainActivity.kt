package com.surya_yasa_antariksa.localdatetime

    import android.annotation.SuppressLint
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.view.View
    import android.widget.ImageButton
    import android.widget.RelativeLayout
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.widget.SearchView
    import androidx.appcompat.widget.SwitchCompat
    import androidx.core.view.GravityCompat
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.RecyclerView.VERTICAL
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.google.android.material.switchmaterial.SwitchMaterial
    import com.surya_yasa_antariksa.localdatetime.database.DateTimeDatabase
    import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private var list = mutableListOf<DateTimeEntity>()
    private lateinit var adapter: UserAdapter
    private lateinit var database: DateTimeDatabase
    private lateinit var findData: SearchView
    private lateinit var parentSearchView: RelativeLayout
    private lateinit var burgerBarIcon: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var switchMaterial: SwitchMaterial


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar

        actionBar?.hide()

        recyclerView = findViewById(R.id.recycle_view)
        addButton = findViewById(R.id.add_button)
        findData = findViewById(R.id.cari_data)
        parentSearchView = findViewById(R.id.cari_data_parent)
        burgerBarIcon = findViewById(R.id.burger_bar)
        drawerLayout = findViewById(R.id.drawer_layout)
        switchMaterial = findViewById(R.id.switch_material)

        database = DateTimeDatabase.getInstance(applicationContext)
        adapter = UserAdapter(list, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)

        addButton.setOnClickListener{
            startActivity(Intent(this, MasukkanDataActivity::class.java))
        }

        switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Switch button diaktifkan
                Toast.makeText(this, "Switch button diaktifkan", Toast.LENGTH_SHORT).show()
            } else {
                // Switch button dinonaktifkan
                Toast.makeText(this, "Switch button dinonaktifkan", Toast.LENGTH_SHORT).show()
            }
        }

        findData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && !query.isEmpty()) {
                    searchByNama(query)
                    findData.clearFocus()
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val nama = newText!!.trim()
                searchByNama(nama)
                return true
            }
        })

        burgerBarIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun searchByNama(nama: String){

        val findNama = database.getDateTimeDao().getAll().filter { it.name!!.contains(nama, true) }

        list.clear()
        list.addAll(findNama)
        if(findNama.isEmpty()){
            Toast.makeText(this, "Nama yang dicari tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()
    }
}