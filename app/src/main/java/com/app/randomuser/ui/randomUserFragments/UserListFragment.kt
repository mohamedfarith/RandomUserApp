package com.app.randomuser.ui.randomUserFragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentUserListBinding
import com.app.randomuser.models.Results
import com.app.randomuser.services.Resource
import com.app.randomuser.ui.randomUserFragments.adapters.UserListAdapter
import com.app.randomuser.ui.bindingAdapter.BindingAdapters
import com.app.randomuser.ui.randomUserFragments.viewmodels.MainActViewModel

class UserListFragment : Fragment(), UserListAdapter.ListItemClickListener {
    private lateinit var navController: NavController
    private lateinit var fragmentBinding: FragmentUserListBinding;
    private lateinit var viewModel: MainActViewModel
    private lateinit var adapter: UserListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActViewModel::class.java)
        navController = Navigation.findNavController(view)
        fragmentBinding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handlePagination(recyclerView, dx, dy)
            }
        })
        fetchData()

    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized)
            fragmentBinding.adapter = adapter
    }


    private fun isOnline(activity: Context): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun fetchData() {
        if (isOnline(fragmentBinding.progressBar.context))
            viewModel.getUserDetails(pageNumber).observe(viewLifecycleOwner, {
                run {
                    val userInfo = it
                    handleDataSource(userInfo)

                }

            })
        else
            loadOfflineData()

    }

    private fun handleDataSource(resource: Resource<ArrayList<Results>>?) {
        when (resource?.status) {
            Resource.Status.SUCCESS -> {
                resource.data?.let { results ->
                    hideProgressBar()
                    populateUi(results)
                }
            }
            Resource.Status.LOADING -> {
                showProgressBar()
            }
            Resource.Status.ERROR -> {
                hideProgressBar()
                loadOfflineData()
            }
        }
    }

    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemConut = 0
    var isScrolling = false
    var pageNumber = 25
    private fun handlePagination(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (isOnline(fragmentBinding.progressBar.context)) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager;
            visibleItemCount = layoutManager.childCount
            totalItemConut = layoutManager.itemCount
            pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
            if (dy > 0) {
                if (isScrolling) {
                    if (visibleItemCount + pastVisibleItems == totalItemConut) {
                        pageNumber += 25
                        fetchData()
                        isScrolling = false
                    }
                }
            }
        }
    }

    private fun loadOfflineData() {
        viewModel.loadDataFromLocal().observe(viewLifecycleOwner, {
            handleDataSource(it)
        })
    }

    private fun showProgressBar() {
        fragmentBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        fragmentBinding.progressBar.visibility = View.GONE
    }

    private fun populateUi(results: ArrayList<Results>) {

        preloadGlideImages(results)
        if (!::adapter.isInitialized) {
            adapter = UserListAdapter(results, this@UserListFragment)
            fragmentBinding.adapter = adapter
        } else {
            adapter.updateItemsList(results)
            adapter.notifyItemRangeChanged(results.size - 25, results.size)
        }

        fragmentBinding.search.setOnClickListener() {
            val bundle = Bundle()
            bundle.putSerializable("resultsList", results)
            navController.navigate(R.id.action_userListFragment_to_searchFragment, bundle)
        }

        fragmentBinding.executePendingBindings()

    }

    private fun preloadGlideImages(results: ArrayList<Results>) {
        if (results.isNotEmpty())
            for (result in results) {
                BindingAdapters.preloadImage(
                    fragmentBinding.progressBar.context,
                    result.picture?.large
                )
            }
    }

    override fun onClick(results: Results) {
        val bundle = Bundle()
        bundle.putSerializable("results", results)
        navController.navigate(R.id.action_userListFragment_to_userInfoFragment, bundle)
    }

}