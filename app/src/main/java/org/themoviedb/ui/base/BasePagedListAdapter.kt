package org.themoviedb.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.BR

abstract class BasePagedListAdapter<T>(
    val onClick: (T) -> Unit = {},
    diffUtil: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, BasePagedListAdapter<T>.ViewHolder>(diffUtil) {

    protected abstract val getLayoutIdRes: Int

    private var items: MutableList<T> = mutableListOf()

    fun onItemClick(item: T) = onClick(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.bind(this)
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdRes

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.apply {
            itemView.setOnClickListener { onItemClick(item) }
            bind(item)
        }
    }

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.apply {
                setVariable(BR.item, item)
                executePendingBindings()
            }
        }

        fun bind(adapter: BasePagedListAdapter<T>) {
            binding.apply {
                setVariable(BR.adapter, adapter)
                executePendingBindings()
            }
        }
    }
}