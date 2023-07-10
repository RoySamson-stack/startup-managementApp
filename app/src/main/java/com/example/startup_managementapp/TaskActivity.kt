package com.example.startup_managementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class TaskActivity : AppCompatActivity() {

        private lateinit var departmentsSpinner: Spinner
        private lateinit var firestore: FirebaseFirestore
        private lateinit var rgTaskStatus: RadioGroup
        private lateinit var btnTask: Button
        private lateinit var etTaskDescription: EditText
        private lateinit var spnDepartments: Spinner



    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_task)


            spnDepartments = findViewById(R.id.spnDepartments)
            rgTaskStatus = findViewById(R.id.rgTaskStatus)
            btnTask = findViewById(R.id.btnTask)
            etTaskDescription = findViewById(R.id.etTaskDescription)
            departmentsSpinner = findViewById(R.id.spnDepartments)
            btnTask = findViewById(R.id.btnTask)
            firestore = FirebaseFirestore.getInstance()

            // Read department names from Firestore and populate the spinner
            firestore.collection("departments").get()
                .addOnSuccessListener { result ->
                    val departments = mutableListOf<String>()

                    for (document in result) {
                        val departmentName = document.getString("name")
                        departmentName?.let { departments.add(it) }
                    }

                    // Create an ArrayAdapter to populate the spinner
                    val adapter = ArrayAdapter(
                        this@TaskActivity,
                        android.R.layout.simple_spinner_item,
                        departments
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    departmentsSpinner.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                }


        btnTask.setOnClickListener {
            val taskDescription = etTaskDescription.text.toString().trim()
            val department = spnDepartments.selectedItem.toString()
            val taskStatus = when (rgTaskStatus.checkedRadioButtonId) {
                R.id.rbHighestPriority -> "High Priority"
                R.id.rbMediumPriority -> "Mid Priority"
                else -> "Low Priority"
            }

            // Create a new task assignment object
            val taskAssignment = hashMapOf(
                "taskDescription" to taskDescription,
                "department" to department,
                "taskStatus" to taskStatus
            )

            // Add the task assignment to the "taskAssignments" collection in Firestore
            firestore.collection("tasks")
                .add(taskAssignment)
                .addOnSuccessListener { taskDocumentReference ->
                    // Get the generated task document ID
                    val taskId = taskDocumentReference.id

                    // Add the task assignment to the corresponding department document in the "departments" collection
                    firestore.collection("departments").document(department)
                        .collection("tasks")
                        .document(taskId)
                        .set(taskAssignment)
                        .addOnSuccessListener {
                            // Data successfully added to both "tasks" and "departments" collections
                            etTaskDescription.text.clear()
                            spnDepartments.setSelection(0)
                            rgTaskStatus.clearCheck()
                            Toast.makeText(this@TaskActivity, "Task assigned successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            // Handle the error
                            Toast.makeText(this@TaskActivity, "Failed to update department tasks", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Toast.makeText(this@TaskActivity, "Task assignment failed", Toast.LENGTH_SHORT).show()
                }
        }



    }
    }