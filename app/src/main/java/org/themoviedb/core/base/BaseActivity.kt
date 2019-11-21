package org.themoviedb.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.support.DaggerAppCompatActivity
import org.themoviedb.utils.ext.setWhiteStatusBar

abstract class BaseActivity<B : ViewDataBinding> constructor(
    @LayoutRes val layoutRes: Int,
    private val isWhiteStatusBar: Boolean = true
) : DaggerAppCompatActivity() {

    protected lateinit var binding: B

    private fun bindView() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isWhiteStatusBar) setWhiteStatusBar()
        bindView()
    }
}