package org.themoviedb.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import org.themoviedb.BR

abstract class BaseAdapter<T>(
    val onClick: (T) -> Unit = {}
) : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {

    private var items: MutableList<T> = mutableListOf()

    protected abstract val getLayoutIdRes: Int

    fun onItemClick(item: T) = onClick(item)

    protected fun removeItemFromList(item: T) {
        val position = getPositionFrom(item) ?: return
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun loadItems(items: List<T>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    private fun getPositionFrom(item: T): Int? {
        var position: Int? = null
        items.forEachIndexed { index, t -> if (t == item) { position = index } }
        return position
    }

    private fun getItemFrom(position: Int): T = items[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItemFrom(position))

    override fun getItemViewType(position: Int): Int = getLayoutIdRes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.bind(this)
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.apply {
                setVariable(BR.item, item)
                executePendingBindings()
            }
        }

        fun bind(adapter: BaseAdapter<T>) {
            binding.apply {
                setVariable(BR.adapter, adapter)
                executePendingBindings()
            }
        }
    }
}
