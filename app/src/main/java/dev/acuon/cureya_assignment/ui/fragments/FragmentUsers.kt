package dev.acuon.cureya_assignment.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dev.acuon.cureya_assignment.databinding.FragmentUsersBinding
import dev.acuon.cureya_assignment.model.User
import dev.acuon.cureya_assignment.ui.adapter.UserClickListener
import dev.acuon.cureya_assignment.ui.adapter.UsersAdapter

class FragmentUsers : Fragment(), UserClickListener {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var usersList: ArrayList<User>
    private lateinit var userAdapter: UsersAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        usersList = ArrayList()
        userAdapter = UsersAdapter(requireContext(), usersList, this@FragmentUsers)
        binding.apply {
            rcvUsers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = userAdapter
            }
        }
        mDbRef.child("users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    usersList.add(currentUser!!)
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onClick(position: Int) {
        Toast.makeText(requireContext(), usersList[position].name.toString()+" clicked", Toast.LENGTH_SHORT).show()
    }

}