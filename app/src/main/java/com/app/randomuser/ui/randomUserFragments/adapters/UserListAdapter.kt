package com.app.randomuser.ui.randomUserFragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.randomuser.R
import com.app.randomuser.databinding.AdapterItemBinding
import com.app.randomuser.models.Results

class UserListAdapter(var itemList: ArrayList<Results>, var clickListener: ListItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)

        return UserListViewHolder(
            DataBindingUtil.inflate(
                inflater,
                R.layout.adapter_item,
                parent,
                false
            )
        )

    }

    fun updateItemsList(newItems: ArrayList<Results>) {
        itemList = newItems;
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is UserListViewHolder) {
            holder.bindData(itemList[holder.bindingAdapterPosition], clickListener)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size;
    }

    class UserListViewHolder(var itemBinding: AdapterItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(result: Results, clickListener: ListItemClickListener) {
            itemBinding.result = result
            itemBinding.click = clickListener;
            itemBinding.executePendingBindings()
        }

    }

    interface ListItemClickListener {

        fun onClick(results: Results)
    }
}