package com.almin.wandroid.ui.base

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.almin.arch.ui.FullScreenDialogFragment
import com.almin.wandroid.R

open class ConfirmFragmentDialog  : FullScreenDialogFragment() {

    var onDialogClickListener: OnDialogClickListener? = null

    companion object{
        private const val TIPS_TEXT = "tips_text"
        private const val CONTENT_TEXT = "content_text"
        private const val LEFT_BTN_TEXT = "left_btn_text"
        private const val RIGHT_BTN_TEXT = "right_btn_text"
        private const val TRUE_OR_CONFIRM_STYLE = "true_or_confirm_style"


        fun instance(tipsText: String?, content: String?, trueOrConfirm: Boolean): ConfirmFragmentDialog {
            val fragment = ConfirmFragmentDialog()
            fragment.apply {
                arguments = bundleOf(TIPS_TEXT to tipsText, CONTENT_TEXT to content, TRUE_OR_CONFIRM_STYLE to trueOrConfirm)
            }
            return fragment
        }

        fun instance(tipsText: String?, content: String?, leftBtnText: String?, rightBtnText: String?): ConfirmFragmentDialog {
            val fragment = ConfirmFragmentDialog()
            fragment.apply {
                arguments = bundleOf(TIPS_TEXT to tipsText, CONTENT_TEXT to content, LEFT_BTN_TEXT to leftBtnText, RIGHT_BTN_TEXT to rightBtnText)
            }
            return fragment
        }
    }

    var hideCancel = false

    override val layoutRes: Int
        get() {
            return R.layout.dialog_fragment_confirm
        }

    override fun initView(rootView: View) {
        arguments?.run {
            val tipsText = getString(TIPS_TEXT)
            val tvTips = rootView.findViewById<TextView>(R.id.tv_tips)
            tvTips.isVisible = !TextUtils.isEmpty(tipsText)
            tvTips.text = tipsText

            val content = getString(CONTENT_TEXT)
            if(content.isNullOrEmpty()){
                rootView.findViewById<TextView>(R.id.tv_content).isVisible = false
            }else{
                rootView.findViewById<TextView>(R.id.tv_content).text = content
            }
            val tvSure = rootView.findViewById<TextView>(R.id.tv_sure)
            val tvCancel = rootView.findViewById<TextView>(R.id.tv_cancel)
            if(getString(LEFT_BTN_TEXT)!=null){
                tvSure.text = getString(RIGHT_BTN_TEXT)
                tvCancel.text = getString(LEFT_BTN_TEXT)
            }else{
                val trueOrConfirmStyle = getBoolean(TRUE_OR_CONFIRM_STYLE)
                tvSure.text = getText(if (trueOrConfirmStyle) R.string.yes else R.string.dialog_confirm)
                tvCancel.text = getText(if (trueOrConfirmStyle) R.string.no else R.string.cancel)
            }
        }

        rootView.setOnClickListener {

        }
        rootView.findViewById<TextView>(R.id.tv_sure).setOnClickListener {
            onDialogClickListener?.onConfirm()
            dismiss()
        }
        rootView.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            onDialogClickListener?.onCancel()
            dismiss()
        }

        if(hideCancel){
            val tvSure = rootView.findViewById<TextView>(R.id.tv_sure)
            val tvCancel = rootView.findViewById<TextView>(R.id.tv_cancel)
            tvCancel.isVisible = false
            tvSure.isVisible = false
            val tvSureFull = rootView.findViewById<TextView>(R.id.tv_sure_full)
            tvSureFull.isVisible = true
            tvSureFull.text = tvSure.text
            tvSureFull.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun initData() {

    }

    override fun onDestroyView() {
        onDialogClickListener = null
        super.onDestroyView()
    }

    interface OnDialogClickListener{
        fun onConfirm()
        fun onCancel()
    }
}