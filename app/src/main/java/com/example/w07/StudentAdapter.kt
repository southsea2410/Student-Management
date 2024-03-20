package com.example.w07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    val onItemClick : ((Student, Int) -> Unit)
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(StudentComparators()) {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.templateName)
        val classView : TextView = itemView.findViewById(R.id.templateClass)
        val genderDOBView : TextView = itemView.findViewById(R.id.templateGenderDOB)
        init {
            itemView.setOnClickListener {
                onItemClick.invoke(getItem(adapterPosition), adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.StudentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
            val template : View = layoutInflater.inflate(R.layout.student_list_item, null)
            return StudentViewHolder(template)
        }

    override fun onBindViewHolder(holder: StudentAdapter.StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.nameView.text = student._name
        holder.classView.text = student._class
        val genderDOB = when(student._gender){
            "M" -> "Male"
            "F" -> "Female"
            "O" -> "Other"
            else -> ""
        } + " - ${student._dob}"
        holder.genderDOBView.text = genderDOB
    }

    class StudentComparators : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }
    }
}