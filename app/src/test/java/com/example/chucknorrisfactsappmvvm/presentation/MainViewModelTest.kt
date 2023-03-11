package com.example.chucknorrisfactsappmvvm.presentation


import com.example.chucknorrisfactsappmvvm.data.Error
import com.example.chucknorrisfactsappmvvm.data.Fact
import com.example.chucknorrisfactsappmvvm.data.Repository
import com.example.chucknorrisfactsappmvvm.data.cache.FactResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var repository: FakeRepository
    private lateinit var viewModel: MainViewModel
    private lateinit var toFavoriteMapper: FakeMapper
    private lateinit var toBaseMapper: FakeMapper
    private lateinit var factUiCallback: FakeFactUiCallback
    private lateinit var dispatchersList: DispatchersList

    @Before
    fun setUp() {
        repository = FakeRepository()
        toFavoriteMapper = FakeMapper(true)
        toBaseMapper = FakeMapper(false)
        factUiCallback = FakeFactUiCallback()
        dispatchersList = FakeDispatchers()

        viewModel = MainViewModel(repository, toFavoriteMapper, toBaseMapper, dispatchersList)
        viewModel.init(factUiCallback)
    }

    @Test
    fun test_successful_not_favorite() {
        repository.returnFetchFactResult =
            FakeFactResult(
                FakeFact("testType", "fakeText", "textPunchline", 5),
                toFavorite = false,
                successful = true,
                errorMessage = ""
            )
        viewModel.getFact()
        val expectedText = "fakeText\ntextPunchline"
        val expectedId = 5

        assertEquals(expectedText, factUiCallback.provideTextList[0])
        assertEquals(expectedId, factUiCallback.provideIconResIdList[0])

        assertEquals(1, factUiCallback.provideTextList.size)
        assertEquals(1, factUiCallback.provideIconResIdList.size)
    }
}

private class FakeFactUiCallback : MainViewModel.FactUiCallback {

    val provideTextList = mutableListOf<String>()

    override fun provideText(text: String) {
        provideTextList.add(text)
    }

    val provideIconResIdList = mutableListOf<Int>()

    override fun provideIconResId(iconResId: Int) {
        provideIconResIdList.add(iconResId)
    }
}

private class FakeDispatchers : DispatchersList {

    private val dispatcher = TestCoroutineDispatcher()

    override fun io(): CoroutineDispatcher = dispatcher


    override fun ui(): CoroutineDispatcher = dispatcher

}

private class FakeMapper(
    private val toFavorite: Boolean
) : Fact.Mapper<FactUi> {

    override suspend fun map(
        type: String,
        setup: String,
        punchline: String,
        id: Int
    ): FactUi {
        return FakeFactUi(setup, punchline, id, toFavorite)
    }
}

private data class FakeFactUi(
    val text: String,
    val punchline: String,
    val id: Int,
    val toFavorite: Boolean
) :
    FactUi(text, punchline, 0)


private data class FakeFact(
    private val type: String,
    private val setup: String,
    private val punchline: String,
    private val id: Int
) : Fact {
    override suspend fun <T> map(mapper: Fact.Mapper<T>): T {
        return mapper.map(type, setup, punchline, id)
    }
}

private data class FakeFactResult(
    private val fact: Fact,
    private val toFavorite: Boolean,
    private val successful: Boolean,
    private val errorMessage: String
) : FactResult {
    override suspend fun <T> map(mapper: Fact.Mapper<T>): T {
        return fact.map(mapper)
    }

    override fun toFavorite(): Boolean = toFavorite

    override fun isSuccessful(): Boolean = successful

    override fun errorMessage(): String = errorMessage
}

private class FakeRepository : Repository<FactUi, Error> {

    var returnFetchFactResult: FactResult? = null

    override suspend fun fetch(): FactResult {
        return returnFetchFactResult!!
    }

    override suspend fun changeFactStatus(): FactUi {
        TODO("Not yet implemented")
    }

    override fun chooseFavorites(favorites: Boolean) {
        TODO("Not yet implemented")
    }
}