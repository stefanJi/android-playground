package io.github.stefanji.playground

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.ac_listview.*

/**
 * Create by jy on 2019-10-31
 */
class ListViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_listview)
        val ada = MAdapter()

        lv.adapter = ada

        repeat(20) {
            ada.data.add(it)
        }
        ada.notifyDataSetChanged()
    }
}


private class MAdapter : BaseAdapter() {

    val data = mutableListOf<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val h: H?
        var v: View? = convertView
        if (v == null) {
            v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            h = H(v)
            v.tag = h
        } else {
            h = v.tag as H
        }
        h.bindView(data[position])
        return v!!
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount(): Int = data.size

    class H(val itemView: View) {
        fun bindView(i: Int) {
            itemView.findViewById<TextView>(android.R.id.text1).text = "$i"
        }
    }

}