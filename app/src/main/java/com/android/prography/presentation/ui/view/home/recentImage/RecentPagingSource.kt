package com.android.prography.presentation.ui.view.home.recentImage

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.prography.BuildConfig.API_KEY
import com.android.prography.data.entity.RecentPhotoResponse
import com.android.prography.domain.usecase.GetRecentImageUseCase
import com.android.prography.presentation.ui.view.home.RecentImageAdapter
import kotlinx.coroutines.delay

class RecentImagePagingSource(
    private val getRecentImageUseCase: GetRecentImageUseCase
) : PagingSource<Int, RecentPhotoResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecentPhotoResponse> {
        return try {
            val page = params.key ?: 1 // 첫 페이지는 1
            val response = getRecentImageUseCase(API_KEY, 10, page) // ✅ 한 페이지당 10개 요청

            response.fold(
                onSuccess = { photos ->
                    delay(2000)
                    LoadResult.Page(
                        data = photos, // 가져온 데이터
                        prevKey = if (page == 1) null else page - 1, // 이전 페이지 (첫 페이지면 null)
                        nextKey = if (photos.isEmpty()) null else page + 1 // 다음 페이지 (없으면 null)
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecentPhotoResponse>): Int? {
        TODO("Not yet implemented")
    }
}
