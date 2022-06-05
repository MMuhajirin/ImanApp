package com.kelp.imanapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kelp.imanapp.databinding.ViewDesignBinding

class Adapter(private var listBrg: ArrayList<data_barang>, context: Context):
RecyclerView.Adapter<Adapter.ViewHolder>(){
    private var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewDesignBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listBrg.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listBrg[position]) {

                val kode = binding.tvKodebrg
                val name = binding.tvNamabrg
                val jml = binding.tvJmlbrg

                val kodeBrg: String? = listBrg.get(position).kodebrg
                val NamaBrg: String? = listBrg.get(position).namabrg
                val JmlBrg: String? = listBrg.get(position).jmlbrg

                holder.binding.tvKodebrg.text = "$kodeBrg"
                holder.binding.tvNamabrg.text = "$NamaBrg"
                holder.binding.tvJmlbrg.text = "$JmlBrg"
                holder.binding.listBarang.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        holder.binding.listBarang.setOnLongClickListener { view ->
                            val action = arrayOf("Update", "Delete")
                            val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                            alert.setItems(action, DialogInterface.OnClickListener { dialog, i ->
                                when (i) {0 -> {
                                    val bundle = Bundle()
                                    bundle.putString("dataKodeBarang", listBrg[holder.getAdapterPosition()].kodebrg)
                                    bundle.putString("dataNamaBarang", listBrg[holder.getAdapterPosition()].namabrg)
                                    bundle.putString("dataJumlahBarang", listBrg[holder.getAdapterPosition()].jmlbrg)
                                    bundle.putString("getPrimaryKey", listBrg[holder.getAdapterPosition()].key)
                                    val intent = Intent(view.context, Update::class.java)
                                    intent.putExtras(bundle)
                                    context.startActivity(intent)
                                }1 -> {
                                    listener?.onDeleteData(listBrg.get(holder.getAdapterPosition()), holder.getAdapterPosition())

                                }
                                }
                            })
                            alert.create()
                            alert.show()
                            true
                        }
                        return true
                    }

                })
            }
        }
    }

    var listener: dataListener? = null
    init {
        this.context = context
        this.listener = context as Show
    }

    interface dataListener: View.OnClickListener {
        fun onDeleteData(data: data_barang?, position: Int)
    }

    @SuppressLint("NotConstructor")
    fun Adapter(listBrg: ArrayList<data_barang>?, context: Context?) {
        this.listBrg = listBrg!!
        this.context = context!!
        listener = context as Show
    }

    inner class ViewHolder(val binding: ViewDesignBinding): RecyclerView.ViewHolder(binding.root)

    init {
        this.context = context
    }
}