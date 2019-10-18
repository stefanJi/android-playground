package io.github.stefanji.playground

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_entrance.*

/**
 * Create by jy on 2019-10-18
 */
class EntranceActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)

        rv_entrance.layoutManager = LinearLayoutManager(this)
        rv_entrance.adapter = Adapter(generateEntrance())
    }

    private fun generateEntrance(): List<String> {
        val packageName = this.packageName
        val className = this.componentName.className
        Log.d("TAG123", "$packageName $className")
        return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            .activities.asSequence()
            .filter { it.name != className }
            .map { it.name }.toList()
    }
}

class Adapter(initData: List<String> = emptyList()) : RecyclerView.Adapter<EntranceHolder>() {

    private val entrances = initData.toMutableList()

    fun updateData(entrancese: List<String>) {
        this.entrances.clear()
        this.entrances.addAll(entrancese)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntranceHolder =
        EntranceHolder(LayoutInflater.from(parent.context).inflate(R.layout.entrance_item, parent, false))

    override fun getItemCount(): Int = entrances.size

    override fun onBindViewHolder(holder: EntranceHolder, position: Int) = holder.bind(entrances[position])
}

class EntranceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(clazz: String) {
        if (itemView is Button) {
            itemView.text = clazz
        }

        itemView.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(itemView.context.packageName, clazz)
            itemView.context.startActivity(intent)
        }
    }
}
