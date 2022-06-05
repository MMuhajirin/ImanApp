package com.kelp.imanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kelp.imanapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()


        binding.btnLogout.setOnClickListener(View.OnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Sesi LogOut Berhasil", Toast.LENGTH_SHORT).show()
                        intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    }

                })
        })

        binding.btnShow.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, Show::class.java))
            finish()
        })

        binding.btnSave.setOnClickListener(View.OnClickListener {
            val getUID = auth!!.currentUser!!.uid
            val db = FirebaseDatabase.getInstance()

            val kode = edKodebrg
            val name = edNamebrg
            val jml = edJmlbrg

            val getKode: String = kode.getText().toString()
            val getName: String = name.getText().toString()
            val getJml: String = jml.getText().toString()

            val getRef: DatabaseReference
            getRef = db.reference

            if (isEmpty(getKode) || isEmpty(getName) || isEmpty(getJml)) {
                Toast.makeText(this@MainActivity, "Field Tidak Boleh Ada yang Kosong!", Toast.LENGTH_SHORT).show()
            } else {
                getRef.child("Admin").child(getUID).child("barang").push()
                    .setValue(data_barang(
                        getKode,
                        getName,
                        getJml
                    )
                    )
                    .addOnCompleteListener(this) {
                        kode.setText("")
                        name.setText("")
                        jml.setText("")
                        Toast.makeText(this@MainActivity, "Sesi Penyimpanan Data Berhasil \n Data Anda Berhasil Disimpan!",
                            Toast.LENGTH_SHORT).show()
                    }
            }
        })

    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }


//    override fun onClick(v: View) {
//        when (v.getId()) {
//            R.id.btnLogout -> {
//
//            }
//            R.id.btnSave -> {
//
//
//            }
//            R.id.btnShow -> {
//
//            }
//        }
//    }
}