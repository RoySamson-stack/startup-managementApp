package com.example.startup_managementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)


        val btnDepartments = findViewById<Button>(R.id.btnDepartments)
        btnDepartments.setOnClickListener {
            val intent = Intent(this@AdminActivity, DepartmentActivity::class.java)
            startActivity(intent)
        }
        val btnTasks = findViewById<Button>(R.id.btnTaskss)
        btnTasks.setOnClickListener {
            val intent = Intent(this@AdminActivity, TasksActivity::class.java)
            startActivity(intent)
        }
        val btnEmployee = findViewById<Button>(R.id.btnEmployees)
        btnEmployee.setOnClickListener {
            val intent = Intent(this@AdminActivity, EmployeeActivity::class.java)
            startActivity(intent)
        }
    }
}