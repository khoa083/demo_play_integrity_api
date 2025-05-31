package com.kblack.base
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
//import com.kblack.base.BR
import java.util.concurrent.Executors


private interface BaseRecyclerAdapter<T : Any, VB : ViewDataBinding> {
    /**
     * get layout res based on view type
     */
    fun getLayoutRes(viewType: Int): Int

    /**
     * bind first time
     * use for set item onClickListener, something only set one time
     */
    fun bindFirstTime(binding: VB) {}

    /**
     * bind view
     */
    fun bindView(binding: VB, item: T, position: Int) {}

}

abstract class BaseListAdapter<T : Any, VB : ViewDataBinding>(
    callBack: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<VB>>(
    AsyncDifferConfig.Builder(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
), BaseRecyclerAdapter<T, VB> {

    override fun submitList(list: List<T>?) {
        super.submitList(ArrayList<T>(list ?: listOf()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        return BaseViewHolder(DataBindingUtil.inflate<VB>(
            LayoutInflater.from(parent.context),
            getLayoutRes(viewType),
            parent,
            false
        ).apply {
            bindFirstTime(this)
        })
    }

//    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
//        val item: T? = getItem(position)
//        holder.binding.setVariable(BR.item, item)
//        if (item != null) {
//            bindView(holder.binding, item, position)
//        }
//        holder.binding.executePendingBindings()
//    }

}


open class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)