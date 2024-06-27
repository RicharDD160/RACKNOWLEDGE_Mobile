package com.example.racknowledge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.racknowledge.User
import com.example.racknowledge.R

class UserAdapter(private var userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // Listener para los botones de actualizar y eliminar
    interface UserActionListener {
        fun onUpdateUser(user: User)
        fun onDeleteUser(user: User)
    }

    private var listener: UserActionListener? = null

    fun setListener(listener: UserActionListener) {
        this.listener = listener
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val tvUserEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        private val btnUpdate: Button = itemView.findViewById(R.id.btnUpdate)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(user: User) {
            tvUserName.text = user.name
            tvUserEmail.text = user.email

            // Acción para el botón de actualizar
            btnUpdate.setOnClickListener {
                listener?.onUpdateUser(user)
            }

            // Acción para el botón de eliminar
            btnDelete.setOnClickListener {
                listener?.onDeleteUser(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(users: List<User>) {
        userList = users
        notifyDataSetChanged()
    }
}
