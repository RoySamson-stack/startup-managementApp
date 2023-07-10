package com.example.startup_managementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)


        val btnTasks = findViewById<Button>(R.id.btnAssignTasks)
        btnTasks.setOnClickListener{
            val intent = Intent(this@TasksActivity, TaskActivity::class.java)
            startActivity(intent)
        }

    }
}