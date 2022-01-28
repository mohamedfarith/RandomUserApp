package com.app.randomuser.ui.randomUserFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentUserInfoBinding
import com.app.randomuser.models.Results

class UserInfoFragment : Fragment() {

    lateinit var fragmentBinding: FragmentUserInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val results = arguments?.get("results")
        results?.let {
            fragmentBinding.results = results as Results

        }


    }


}