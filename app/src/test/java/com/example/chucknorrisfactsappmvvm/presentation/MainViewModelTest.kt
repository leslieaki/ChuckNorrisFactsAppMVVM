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

        viewModel = MainViewModel(
            repository,
            toFavoriteMapper,
            toBaseMapper,
            dispatchersList
        )
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
        val expectedText = "fakeText_textPunchline"
        val expectedId = 5

        assertEquals(expectedText, factUiCallback.provideTextList[0])
        assertEquals(expectedId, factUiCallback.provideIconResIdList[0])

        assertEquals(1, factUiCallback.provideTextList.size)
        assertEquals(1, factUiCallback.provideIconResIdList.size)
    }

    @Test
    fun test_successful_favorite() {
        repository.returnFetchFactResult =
            FakeFactResult(
                FakeFact("testType", "fakeText", "textPunchline", 15),
                toFavorite = true,
                successful = true,
                errorMessage = ""
            )
        viewModel.getFact()
        val expectedText = "fakeText_textPunchline"
        val expectedId = 16

        assertEquals(expectedText, factUiCallback.provideTextList[0])
        assertEquals(expectedId, factUiCallback.provideIconResIdList[0])

        assertEquals(1, factUiCallback.provideTextList.size)
        assertEquals(1, factUiCallback.provideIconResIdList.size)
    }

    @Test
    fun test_not_successful() {
        repository.returnFetchFactResult =
            FakeFactResult(
                FakeFact("testType", "fakeText", "textPunchline", 15),
                toFavorite = true,
                successful = false,
                errorMessage = "testErrorMessage"
            )
        viewModel.getFact()
        val expectedText = "testErrorMessage\n"
        val expectedId = 0

        assertEquals(expectedText, factUiCallback.provideTextList[0])
        assertEquals(expectedId, factUiCallback.provideIconResIdList[0])

        assertEquals(1, factUiCallback.provideTextList.size)
        assertEquals(1, factUiCallback.provideIconResIdList.size)
    }

    @Test
    fun test_change_fact_status() {
        repository.returnChangeFactStatus = FakeFactUi("testText", "testPunchline", 20, false)
        viewModel.changeFactStatus()

        val expectedText = "testText_testPunchline"
        val expectedId = 20

        assertEquals(expectedText, factUiCallback.provideTextList[0])
        assertEquals(expectedId, factUiCallback.provideIconResIdList[0])

        assertEquals(1, factUiCallback.provideTextList.size)
        assertEquals(1, factUiCallback.provideIconResIdList.size)
    }

    @Test
    fun test_choose_favorite() {
        viewModel.chooseFavorite(true)
        assertEquals(true, repository.chooseFavoritesList[0])
        assertEquals(1, repository.chooseFavoritesList.size)

        viewModel.chooseFavorite(false)
        assertEquals(false, repository.chooseFavoritesList[1])
        assertEquals(2, repository.chooseFavoritesList.size)
    }
}

private class FakeFactUiCallback : FactUiCallback {

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
        createdDate: String,
        iconUrl: String,
        id: String,
        updateDate: String,
        url: String,
        value: String
    ): FactUi {
        return FakeFactUi(createdDate, iconUrl, id, updateDate, url, value, toFavorite)
    }
}

private data class FakeFactUi(
    private val createdDate: String,
    private val iconUrl: String,
    private val id: String,
    private val updateDate: String,
    private val url: String,
    private val value: String,
    private val toFavorite: Boolean
) : FactUi {

    override fun show(factUiCallback: FactUiCallback) = with(factUiCallback) {
        provideText(value)
        provideIconResId(if (toFavorite) id + 1 else id)
    }
}


private data class FakeFact(
    private val createdDate: String,
    private val iconUrl: String,
    private val id: String,
    private val updateDate: String,
    private val url: String,
    private val value: String
) : Fact {
    override suspend fun <T> map(mapper: Fact.Mapper<T>): T {
        return mapper.map(createdDate, iconUrl, id, updateDate, url, value)
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

    var returnChangeFactStatus: FactUi? = null

    override suspend fun changeFactStatus(): FactUi {
        return returnChangeFactStatus!!
    }

    var chooseFavoritesList = mutableListOf<Boolean>()

    override fun chooseFavorites(favorites: Boolean) {

        chooseFavoritesList.add(favorites)
    }
}