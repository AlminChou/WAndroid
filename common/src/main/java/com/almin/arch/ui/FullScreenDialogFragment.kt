package com.almin.arch.ui
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.almin.common.R

/**
 * Created by Almin
 * 全屏简易安全的fragment dialog，注意 用复写的show方法避免泄露
 */
abstract class FullScreenDialogFragment : DialogFragment() {
    var onDialogListener: OnDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen)
    }

    protected abstract val layoutRes: Int

    protected abstract fun initView(rootView: View)
    protected abstract fun initData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return if (layoutRes > 0) inflater.inflate(layoutRes, container, false) else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        initData()
    }

    fun show(fragmentManager: FragmentManager?) {
        try {
            if (fragmentManager != null && !fragmentManager.isStateSaved) {
                show(fragmentManager, javaClass.simpleName)
            }
        } catch (e: IllegalStateException) {
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogListener?.onDismiss()
        super.onDismiss(dialog)
    }

    interface OnDialogListener {
        fun onDismiss()
    }
}