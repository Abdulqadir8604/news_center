package com.lamaq.newscenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        val data = ArrayList<Model>()

        for (i in 0..10) {
            data.add(Model("Title $i", "Description $i", "https://picsum.photos/200/300?random=$i"))
        }
        recyclerView.adapter = Adapter(data)
    }
}