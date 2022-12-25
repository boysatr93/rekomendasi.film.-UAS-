package com.boysatria.rekomendasifilm.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boysatria.rekomendasifilm.databinding.ItemLoadingStateBinding

class FooterLoadingAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<FooterLoadingAdapter.LoadingStateViewHolder>() {
    open inner class LoadingStateViewHolder(val binding: ItemLoadingStateBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState){
            if (loadState is LoadState.Error){
                binding.tvErrorMessage.text = loadState.error.localizedMessage
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.tvErrorMessage.isVisible = loadState !is LoadState.Loading
            binding.btnRetry.isVisible = loadState !is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = LayoutInflater.from(parent.context)

        return LoadingStateViewHolder(
            ItemLoadingStateBinding.inflate(view, parent, false)
        )
    }
}