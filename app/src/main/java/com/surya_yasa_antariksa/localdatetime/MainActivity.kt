package com.surya_yasa_antariksa.localdatetime

    import android.annotation.SuppressLint
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.view.MenuItem
    import android.view.View
    import android.widget.ImageButton
    import android.widget.RelativeLayout
    import android.widget.Toast
    import androidx.appcompat.app.ActionBarDrawerToggle
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.widget.SearchView
    import androidx.appcompat.widget.SwitchCompat
    import androidx.core.view.GravityCompat
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
    import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.RecyclerView.VERTICAL
    import com.google.android.material.floatingactionbutton.FloatingActionButton
    import com.google.android.material.navigation.NavigationView
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
    private lateinit var drawerToggle: ActionBarDrawerToggle
    var isShortByLatest = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycle_view)
        addButton = findViewById(R.id.add_button)
        findData = findViewById(R.id.cari_data)
        parentSearchView = findViewById(R.id.cari_data_parent)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "daftar pesanan"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu_icon)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.terbaru -> {
                    isShortByLatest = true
                    getData()
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                }
                R.id.terlama -> {
                    isShortByLatest = false
                    getData()
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                }
                R.id.abjad -> {
                    getData()
                    list.sortBy { it.name }
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                }
                else ->Toast.makeText(applicationContext, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }
            true
    }

        database = DateTimeDatabase.getInstance(applicationContext)
        adapter = UserAdapter(list, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)

        addButton.setOnClickListener{
            startActivity(Intent(this, MasukkanDataActivity::class.java))
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
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData() {
        list.clear()
        list.addAll(if (isShortByLatest) database.getDateTimeDao().getAllByLatest() else database.getDateTimeDao().getAllByOldest())
        adapter.notifyDataSetChanged()
    }

    override fun onUpdateClick(position: Int) {
        val dateTimeEntity = list[position]
        startEditActivity(dateTimeEntity)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(drawerToggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun startEditActivity(dateTimeEntity: DateTimeEntity){
        val intent = Intent(this, EditDataActivity::class.java)
        intent.putExtra("id", dateTimeEntity.uid)
        intent.putExtra("nama", dateTimeEntity.name)
        intent.putExtra("tanggal", dateTimeEntity.localDateTime)
        intent.putExtra("kegiatan", dateTimeEntity.kegiatan)
        startActivity(intent)
    }
}