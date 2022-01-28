package com.app.randomuser.ui.randomUserFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentSearchBinding
import com.app.randomuser.models.Results
import com.app.randomuser.ui.randomUserFragments.adapters.UserListAdapter


class SearchFragment : Fragment(), UserListAdapter.ListItemClickListener {
    private lateinit var fragmentBinding: FragmentSearchBinding
    private lateinit var results: ArrayList<Results>
    private var matchedResults: ArrayList<Results> = arrayListOf()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        results = arguments?.get("resultsList") as ArrayList<Results>
        fragmentBinding.searchAdapter = UserListAdapter(results, this)
        fragmentBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText?.trim())
                return true
            }

        })

    }

    private fun search(query: String?) {
        matchedResults.clear()
        query?.let {
            if (results.isNotEmpty()) {
                results.forEach { person ->
                    if (person.name?.getFullName()?.contains(query, true) == true) {
                        matchedResults.add(person)
                    }
                }
                updateRecyclerView()
            }
        }
    }

    private fun updateRecyclerView() {
        fragmentBinding.searchAdapter?.updateItemsList(matchedResults)
        fragmentBinding.searchAdapter?.notifyDataSetChanged()
    }

    override fun onClick(results: Results) {
        val bundle = Bundle()
        bundle.putSerializable("results", results)
        navController.navigate(R.id.action_searchFragment_to_userInfoFragment, bundle)
    }
}