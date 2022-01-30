package com.app.randomuser.ui.randomUserFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.randomuser.Constants
import com.app.randomuser.GenericCallback
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentUserListBinding
import com.app.randomuser.models.Results
import com.app.randomuser.services.Resource
import com.app.randomuser.ui.bindingAdapter.BindingAdapters
import com.app.randomuser.ui.randomUserFragments.adapters.UserListAdapter
import com.app.randomuser.ui.randomUserFragments.viewmodels.MainActViewModel
import com.app.randomuser.utils.LocalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListFragment : Fragment(), UserListAdapter.ListItemClickListener {
    private lateinit var navController: NavController
    private lateinit var fragmentBinding: FragmentUserListBinding;
    private lateinit var viewModel: MainActViewModel
    private lateinit var adapter: UserListAdapter
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var isScrolling = false
    private var pageNumber = Constants.DEFAULT_PAGE_LIMIT
    private var updatedItemCount = 0

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
        viewModel = ViewModelProvider(this)[MainActViewModel::class.java]
        //initiating nav controller
        navController = Navigation.findNavController(view)

        //adding on scroll listener to handle pagination
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


    private fun fetchData() {
        viewModel.getUserDetails(fragmentBinding.progressBar.context, pageNumber)
            .observe(viewLifecycleOwner, {
                run {
                    val userInfo = it
                    handleDataSource(userInfo)

                }

            })


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
                activity?.let {
                    LocalUtils.showAlertDialog(it,
                        resources.getString(R.string.connection_error),
                        resources.getString(R.string.internet_connection_error),
                        object : GenericCallback<String> {
                            override fun callback(data: String) {
                                if (data == "positive") {
                                    fetchData()
                                }
                            }
                        })

                }
            }
        }
    }


    private fun handlePagination(recyclerView: RecyclerView, dx: Int, dy: Int) {

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager;
        visibleItemCount = layoutManager.childCount
        totalItemCount = layoutManager.itemCount
        pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        if (dy > 0) {
            if (isScrolling) {
                if (visibleItemCount + pastVisibleItems == totalItemCount) {
                    pageNumber += 25
                    fetchData()
                    isScrolling = false
                }
            }
        }

    }


    private fun showProgressBar() {
        fragmentBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        fragmentBinding.progressBar.visibility = View.GONE
    }

    private fun populateUi(results: ArrayList<Results>) {

        lifecycleScope.launch(Dispatchers.Main) {
            //preloading images for smooth loading in offline mode
            preloadGlideImages(results)
            //checking and initiating user list adapter
            if (!::adapter.isInitialized) {
                adapter = UserListAdapter(results, this@UserListFragment)
                fragmentBinding.adapter = adapter
            } else {
                adapter.updateItemsList(results)
                if (results.size - Constants.DEFAULT_PAGE_LIMIT > 0 && updatedItemCount != results.size) {
                    adapter.notifyItemRangeInserted(
                        results.size - Constants.DEFAULT_PAGE_LIMIT,
                        results.size
                    )
                    updatedItemCount = results.size
                }
            }
        }


        // click listener for search option
        fragmentBinding.search.tag = results
        fragmentBinding.search.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("resultsList", it.tag as ArrayList<*>)
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

    //onclick action triggered from user list adapter
    override fun onClick(results: Results) {
        val bundle = Bundle()
        bundle.putSerializable("results", results)
        navController.navigate(R.id.action_userListFragment_to_userInfoFragment, bundle)
    }

}