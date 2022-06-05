package com.kelp.imanapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kelp.imanapp.databinding.ActivityLoginBinding
import com.kelp.imanapp.databinding.ActivityMainBinding
import com.kelp.imanapp.databinding.ActivityShowBinding
import kotlinx.android.synthetic.main.activity_show.*

class Show : AppCompatActivity(), Adapter.dataListener {

    private var auth: FirebaseAuth? = null
    val db = FirebaseDatabase.getInstance()

    private var dataBrg = ArrayList<data_barang>()

    private var adapter: RecyclerView.Adapter<*>? = null
    private var rV: RecyclerView? = null
    private var lM: RecyclerView.LayoutManager? = null

    private lateinit var binding: ActivityShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBack1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        })

        rV = binding.datalist
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Please Wait a sec...", Toast.LENGTH_SHORT).show()
        val getUID: String = auth?.getCurrentUser()?.getUid().toString()
        val getRef = db.getReference()
        getRef.child("Admin").child(getUID).child("barang")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (datasnapshot.exists()) {
                        dataBrg.clear()
                        for (snapshot in datasnapshot.children) {
                            val brg = snapshot.getValue(data_barang::class.java)
                            brg?.key = snapshot.key
                            dataBrg.add(brg!!)
                        }
                        adapter = Adapter(dataBrg, this@Show)
                        rV?.adapter = adapter
                        (adapter as Adapter).notifyDataSetChanged()
                        Toast.makeText(
                            applicationContext,
                            "Data Berhasil Dimuat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Pemuatan Data Gagal!!", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(
                        "Show", databaseError.details + " " +
                                databaseError.message
                    )
                }

            })
    }

    private fun MyRecyclerView() {
        lM = LinearLayoutManager(this)
        datalist?.layoutManager = lM
        datalist?.setHasFixedSize(true)

        val itemDec = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(applicationContext, R.drawable.line!!)?.let {
            itemDec.setDrawable(
                it
            )
        }
        datalist?.addItemDecoration(itemDec)
    }

    override fun onDeleteData(data: data_barang?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        if (getReference != null) {
            getReference.child("Admin")
                .child(getUserID)
                .child("barang")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@Show, "Data Berhasil Dihapus", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(this@Show, "Reference kosong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}