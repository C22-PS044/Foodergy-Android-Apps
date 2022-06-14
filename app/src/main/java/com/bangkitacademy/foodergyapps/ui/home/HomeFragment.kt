package com.bangkitacademy.foodergyapps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.util.rangeTo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkitacademy.foodergyapps.R
import com.bangkitacademy.foodergyapps.data.model.DataHistory
import com.bangkitacademy.foodergyapps.databinding.FragmentHomeBinding
import com.bangkitacademy.foodergyapps.view.login.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val list = ArrayList<DataHistory>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvStory
        recyclerView.adapter = HomeAdapter(list)
        binding.rvStory.layoutManager = LinearLayoutManager(context)
        list.addAll(listUseres)

        val textView: TextView = binding.nameHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
    private val listUseres: ArrayList<DataHistory>
        get() {
            val dataNameKemasan = resources.getStringArray(R.array.namaKemasan)
            val datakomposisi = resources.getStringArray(R.array.alergi)
            val dataRekomendasi = resources.getStringArray(R.array.rekomendasi)
            val listUser = ArrayList<DataHistory>()
            for (position in dataNameKemasan.indices) {
                val user = DataHistory(
                    dataNameKemasan[position],
                    datakomposisi[position],
                    dataRekomendasi[position]
                )
                listUser.add(user)
            }
            return listUser
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}