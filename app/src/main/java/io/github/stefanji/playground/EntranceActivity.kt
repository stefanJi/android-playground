package io.github.stefanji.playground

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        requestPermission()

        rv_entrance.layoutManager = LinearLayoutManager(this)
        rv_entrance.adapter = Adapter(generateEntrance())
    }

    private fun generateEntrance(): List<ActivityItem> {
        val packageName = this.packageName
        val className = this.componentName.className
        Log.d("TAG123", "$packageName $className")
        return packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            .activities.asSequence()
            .filter { it.name != className }
            .filter { it.name.contains(packageName) }
            .map {
                ActivityItem(it.name.split(".").last(), it.name)
            }.toList()
    }

    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.RECORD_AUDIO
        ).filter { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED }.toTypedArray()
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, 500)
        }
    }
}

class Adapter(initData: List<ActivityItem> = emptyList()) : RecyclerView.Adapter<EntranceHolder>() {

    private val entrances = initData.toMutableList()

    fun updateData(entrancese: List<ActivityItem>) {
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
    fun bind(item: ActivityItem) {
        if (itemView is Button) {
            itemView.text = item.name
        }

        itemView.setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName(itemView.context.packageName, item.className)
            itemView.context.startActivity(intent)
        }
    }
}

data class ActivityItem(val name: String, val className: String)