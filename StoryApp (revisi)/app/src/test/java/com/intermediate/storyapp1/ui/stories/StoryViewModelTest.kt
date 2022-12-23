package com.intermediate.storyapp1.ui.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.intermediate.storyapp1.data.adapter.ListStoriesAdapter
import com.intermediate.storyapp1.data.model.Story
import com.intermediate.storyapp1.data.presenter.StoryRepository
import com.intermediate.storyapp1.ui.detail.createStory.CreateStoryViewmodel
import com.intermediate.storyapp1.utils.DataDummy
import com.intermediate.storyapp1.utils.MainDispatcherRule
import com.intermediate.storyapp1.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    val storiesViewModel = StoryViewModel(storyRepository)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private var dummyStory = DataDummy.generateDummyStory()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Story Should Not Null and Return Success`() = mainDispatcherRules.runBlockingTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory.listStory)
        val expectedResponse = MutableLiveData<PagingData<Story>>()

        expectedResponse.value = data
        Mockito.`when`(storyRepository.getListStories()).thenReturn(expectedResponse)


        val actualStory: PagingData<Story> = storiesViewModel.getListStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.listStory, differ.snapshot())
        Assert.assertEquals(dummyStory.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory.listStory[0].name, differ.snapshot()[0]?.name)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}