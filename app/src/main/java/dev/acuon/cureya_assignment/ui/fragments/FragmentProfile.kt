package dev.acuon.cureya_assignment.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dev.acuon.cureya_assignment.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userDetails: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        userDetails = ArrayList()

        mDbRef.child("users").child(mAuth.currentUser!!.uid).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userDetails.clear()
                for(postSnapshot in snapshot.children) {
                    userDetails.add(postSnapshot.getValue(String::class.java)!!)
                }
                binding.apply {
                    userName.text = userDetails[1]
                    userEmail.text = userDetails[0]
                    userUid.text = userDetails[2]
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}