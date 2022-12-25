package com.boysatria.rekomendasifilm.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

//BaseFragment merupakan class base/pondasi dari sebuah fragment yang sudah dipasang data binding
abstract class BaseFragment<B : ViewBinding> : Fragment() {

	private var viewBinding: B? = null

	protected val binding: B
		get() = checkNotNull(viewBinding)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val binding = onInflateView(inflater, container)
		viewBinding = binding
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

	}

	override fun onDestroyView() {
		viewBinding = null
		super.onDestroyView()
	}

	open fun getTitle(): CharSequence? = null

	override fun onAttach(context: Context) {
		super.onAttach(context)
		getTitle()?.let {
			activity?.title = it
		}
	}

	protected fun bindingOrNull() = viewBinding

	protected abstract fun onInflateView(inflater: LayoutInflater, container: ViewGroup?): B
}
