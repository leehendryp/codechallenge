package com.leehendryp.codechallenge.features.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.leehendryp.codechallenge.features.list.domain.Album
import kotlinx.coroutines.flow.Flow

internal object AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean = oldItem == newItem
}

internal object ListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

internal suspend fun Flow<PagingData<Album>>.albumSnapshot(): List<Album>? {
    var albums: List<Album>? = null

    val differ = AsyncPagingDataDiffer(AlbumDiffCallback, ListUpdateCallback).apply {
        addOnPagesUpdatedListener {
            albums = snapshot().items
        }
    }

    collect { pagingData ->
        differ.submitData(pagingData)
    }

    return albums
}
