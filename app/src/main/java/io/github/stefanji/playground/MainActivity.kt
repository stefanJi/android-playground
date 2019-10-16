package io.github.stefanji.playground

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_1.*
import kotlinx.android.synthetic.main.activity_1.recyclerListTest
import kotlinx.android.synthetic.main.innrer_scroll_test.*
import java.util.*


private const val TAG = "Main"

class MainActivity : AppCompatActivity() {

    lateinit var testAdapter: TestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.innrer_scroll_test)

        testAdapter = TestAdapter()

        recyclerListTest.adapter = testAdapter
        recyclerListTest.layoutManager = LinearLayoutManager(this)

        testAdapter.data = arrayListOf(
            ItemData("a"),
            ItemData("b"),
            ItemData("c"),
            ItemData("d"),
            ItemData("e"),
            ItemData("f"),
            ItemData("g")
        )

        insertBtn.setOnClickListener {
            testAdapter.data.add(0, ItemData("${Random().nextInt()}"))
            testAdapter.notifyItemInserted(0)
        }
    }

    private fun setActions() {
        btnUpdatePosition.setOnClickListener {
            val position = 0
            testAdapter.data[position].complete = true
            testAdapter.notifyItemChanged(position)
            recyclerListTest.postDelayed({
                val d = testAdapter.data.removeAt(position)
                testAdapter.data.add(d)
                testAdapter.notifyItemMoved(position, testAdapter.itemCount - 1)
            }, 700)
        }
    }

    private fun setupProgressBar() {
        progressBar.max = 3
        progressBar.progressDrawable.apply {
            highLightColor = Color.GREEN
            defaultColor = Color.GRAY
            intervalColor = Color.BLACK
            intervalHighLightColor = Color.MAGENTA
        }
        btnIncreProgress.setOnClickListener {
            progressBar.setProgress(progressBar.progress + 1, true)
        }
        btnDeProgress.setOnClickListener {
            progressBar.setProgress(progressBar.progress - 1)
        }
    }
}

class TestAdapter : RecyclerView.Adapter<TestAdapter.Holder>() {

    var data = mutableListOf<ItemData>()
        set(value) {
            data.clear()
            data.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val icon: ImageView = itemView.findViewById(R.id.ivIcon)
        var complete = false
        var shownAnimation = false

        override fun toString(): String {
            return title.text.toString()
        }

        fun bind(s: ItemData) {
            val tag = "position: $adapterPosition [${s.title}] ${s.complete} ${s.shownAnimation}"
            title.text = s.title
            complete = s.complete
            shownAnimation = s.shownAnimation
            itemView.tag = tag
            Log.d(TAG, "onBind $tag")
        }

    }
}

data class ItemData(
    val title: String,
    var complete: Boolean = false,
    var shownAnimation: Boolean = false
)
