package com.kblack.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VB: ViewDataBinding, VM: BaseViewModel> : Fragment() {
    private var _binding: VB? = null
    protected val fragmentBinding get() = _binding!!
    protected abstract val mViewModel: VM

//    ViewBinding abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    /**
     * override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGeneratePassphraseBinding =
     *     FragmentGeneratePassphraseBinding::inflate
     */

    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        ViewBinding _binding = bindingInflater(inflater, container, false)
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(fragmentBinding)
    }

    abstract fun setupView(fragmentBinding: VB)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun findFragment(tag: String): Fragment? {
        return activity?.supportFragmentManager?.findFragmentByTag(tag)
    }

    protected fun findChildFragment(parentFragment: Fragment = this, tag: String): Fragment? {
        return parentFragment.childFragmentManager.findFragmentByTag(tag)
    }

}