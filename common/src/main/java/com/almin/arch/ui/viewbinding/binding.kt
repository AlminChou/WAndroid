package com.almin.arch.ui.viewbinding

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun <VB : ViewBinding> ComponentActivity.viewBinding(inflate: (LayoutInflater) -> VB) = lazy {
  inflate(layoutInflater).also { binding ->
//    setContentView(com.almin.arch.ui.viewbinding.binding.root)
    if (binding is ViewDataBinding) binding.lifecycleOwner = this
  }
}

fun <VB : ViewBinding> Fragment.viewBinding(bind: (View) -> VB) = FragmentBindingDelegate(bind)

fun <VB : ViewBinding> Dialog.binding(inflate: (LayoutInflater) -> VB) = lazy {
  inflate(layoutInflater).also { setContentView(it.root) }
}

fun <VB : ViewBinding> ViewGroup.viewBinding(
  inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
  attachToParent: Boolean = false
) = lazy {
  inflate(LayoutInflater.from(context), if (attachToParent) this else null, attachToParent)
}

fun <VB : ViewBinding> ViewGroup.inflate(inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) =
  inflate(LayoutInflater.from(context), this, true)

fun <VB : ViewBinding> TabLayout.Tab.setCustomView(inflate: (LayoutInflater) -> VB, block: VB.() -> Unit) {
  customView = inflate(LayoutInflater.from(parent!!.context)).apply(block).root
}

fun <VB : ViewBinding> TabLayout.doOnCustomTabSelected(
  bind: (View) -> VB,
  onTabUnselected: (VB.(TabLayout.Tab) -> Unit)? = null,
  onTabReselected: (VB.(TabLayout.Tab) -> Unit)? = null,
  onTabSelected: (VB.(TabLayout.Tab) -> Unit)? = null,
) =
  addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
      tab.getBinding(bind)?.let { onTabSelected?.invoke(it, tab) }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
      tab.getBinding(bind)?.let { onTabUnselected?.invoke(it, tab) }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
      tab.getBinding(bind)?.let { onTabReselected?.invoke(it, tab) }
    }
  })

@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> TabLayout.Tab.getBinding(bind: (View) -> VB): VB? =
  (tag as? VB) ?: customView?.let { bind(it) }?.also { tag = it }

fun <VB : ViewBinding> NavigationView.setHeaderView(index: Int = 0, bind: (View) -> VB, block: VB.() -> Unit) =
  getHeaderView(index)?.let { bind(it) }?.run(block)

interface BindingLifecycleOwner {
  fun onDestroyViewBinding(destroyingBinding: ViewBinding)
}

class FragmentBindingDelegate<VB : ViewBinding>(private val bind: (View) -> VB) : ReadOnlyProperty<Fragment, VB> {
  private var binding: VB? = null

  override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
    if (binding == null) {
      binding = try {
        bind(thisRef.requireView()).also { binding ->
          if (binding is ViewDataBinding) binding.lifecycleOwner = thisRef.viewLifecycleOwner
        }
      } catch (e: IllegalStateException) {
        throw IllegalStateException("The com.almin.arch.ui.viewbinding.binding property has been destroyed.")
      }
      thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver{
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
          if (thisRef is BindingLifecycleOwner) thisRef.onDestroyViewBinding(binding!!)
          binding = null
        }
      })
    }
    return binding!!
  }
}