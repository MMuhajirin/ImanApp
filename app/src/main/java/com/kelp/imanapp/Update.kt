package com.kelp.imanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kelp.imanapp.databinding.ActivityUpdateBinding

class Update : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding

    private var db: DatabaseReference? = null
    private var auth: FirebaseAuth? = null

    private var cekKod: String? = null
    private var cekNam: String? = null
    private var cekJml: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBack2.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, Show::class.java))
        })

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        data
        binding.btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekKod = binding.newEdKodebrg.getText().toString()
                cekNam = binding.newEdNamebrg.getText().toString()
                cekJml = binding.newEdJmlbrg.getText().toString()

                if (isEmpty(cekKod!!) || isEmpty(cekNam!!) || isEmpty(cekJml!!)) {
                    Toast.makeText(this@Update, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setDatabrg = data_barang()
                    setDatabrg.kodebrg = binding.newEdKodebrg.getText().toString()
                    setDatabrg.namabrg = binding.newEdNamebrg.getText().toString()
                    setDatabrg.jmlbrg = binding.newEdJmlbrg.getText().toString()
                    updateData(setDatabrg)
                }
            }

        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
    private get() {
        val getKod = intent.extras!!.getString("dataKodeBarang")
        val getNam = intent.extras!!.getString("dataNamaBarang")
        val getJml = intent.extras!!.getString("dataJumlahBarang")

        binding.newEdKodebrg!!.setText(getKod)
        binding.newEdNamebrg!!.setText(getNam)
        binding.newEdJmlbrg!!.setText(getJml)
    }

    private fun updateData(barang: data_barang) {
        val UID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        db!!.child("Admin")
            .child(UID!!)
            .child("barang")
            .child(getKey!!)
            .setValue(barang)
            .addOnSuccessListener {
                binding.newEdKodebrg!!.setText("")
                binding.newEdNamebrg!!.setText("")
                binding.newEdJmlbrg!!.setText("")
                Toast.makeText(this@Update, "Data Berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}