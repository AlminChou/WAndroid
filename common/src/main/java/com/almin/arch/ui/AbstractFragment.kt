package com.almin.arch.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.viewbinding.ViewBinding
import com.almin.arch.ktx.collect
import com.almin.arch.ui.lifecycleobserver.ActivityCreatedObserver
import com.almin.arch.viewmodel.AbstractViewModel
import com.almin.arch.viewmodel.Contract.PageEffect
import com.almin.arch.viewmodel.Contract.PageState


/**
 * Created by Almin on 2019-07-17.
 */
abstract class AbstractFragment<VB : ViewBinding, S : PageState, Effect : PageEffect, VM : AbstractViewModel<S, *, Effect>>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment(), ActivityCreatedObserver {

    companion object {
        val TAG: String = this::class.java.simpleName
    }

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected abstract val viewModel: VM
//    protected abstract val layoutRes: Int

    private var onBackPressedCallback: OnBackPressedCallback? = null
    private val onBackPressedDispatcher: OnBackPressedDispatcher by lazy { requireActivity().onBackPressedDispatcher }

    protected abstract fun initView(rootView: View)
    protected abstract fun initData()
//    protected abstract fun initEventSubscribe(bus: FlowBus)

    protected abstract fun handleState(state: S)
    protected abstract fun handleEffect(effect: Effect)


    protected open fun addBackPressedCallback(): Boolean = true

    // true   handle by myself
    // false  deliver to parent
    protected open fun handleOnBackPressed(): Boolean {
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.attach(arguments)
        setHasOptionsMenu(true)
        _binding = inflate(inflater, container, false)
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
//        return if (layoutRes > 0) inflater.inflate(layoutRes, container, false) else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)

        if (addBackPressedCallback()) {
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    println("current fragment  handleOnBackPressed ${this@AbstractFragment}")
                    if (!this@AbstractFragment.handleOnBackPressed()) {
                        println("current fragment  handleOnBackPressed  int   ${this@AbstractFragment}")

                        // one
//                        onBackPressedCallback?.isEnabled = false  // 消费完一定要设置成false 不然内部不会被消费 一直持有callback对象 泄露
//                         往上层传递 递归给其他看看谁消费
//                        onBackPressedDispatcher.onBackPressed()

//                        activity?.supportFragmentManager?.popBackStack()
                        // two
//                        if(isAdded){
                        findNavController(this@AbstractFragment).popBackStack()
//                        }
                    }
                }
            }
            //addCallback 需要 传入 viewLifecycleOwner 自动反注册callback 否则要 onBackPressedCallback?.isEnabled = false
            onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)
        }

        viewModel.uiState.collect(
            lifecycleOwner = viewLifecycleOwner,
            Lifecycle.State.STARTED
        ) { it: S? ->
            it?.run {
                handleState(this)
            }
        }

        viewModel.effect.collect(lifecycleOwner = viewLifecycleOwner, Lifecycle.State.STARTED) {
            handleEffect(it as Effect)
        }
        initData()
    }


    override fun onResume() {
        super<Fragment>.onResume()

//        onBackPressedCallback?.isEnabled = true
    }

    override fun onActivityCreated() {
//        initEventSubscribe(FlowBus())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    protected open fun showMessage(msg: String) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}