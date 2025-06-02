package com.kblack.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VB: ViewDataBinding, VM: BaseViewModel> : AppCompatActivity() {
    private var _binding: VB? = null
    protected val activityBinding get() = _binding!!

    abstract val viewModel: VM
    abstract val layoutId: Int
    abstract val idView: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        activityBinding.lifecycleOwner = this
        setupView(activityBinding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(idView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    abstract fun setupView(activityBinding: VB)

    abstract fun showView(isShow: Boolean)

    abstract fun setStatusBar()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}