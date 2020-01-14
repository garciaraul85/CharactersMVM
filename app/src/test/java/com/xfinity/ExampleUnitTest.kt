package com.xfinity

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.xfinity.data.DataManager
import com.xfinity.data.model.response.Icon
import com.xfinity.data.model.response.RelatedTopic
import com.xfinity.data.remote.CharactersService
import com.xfinity.features.masterdetail.CharacterListViewModel
import io.reactivex.Single.just
/*import duponchel.nicolas.rxbasics.MainViewModel.InstallationStatus.SUCCESS
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.LOADING
import duponchel.nicolas.rxbasics.MainViewModel.LoadingStatus.NOT_LOADING*/
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.Mockito.`when`


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    // Run code sync
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Mock
    private lateinit var  charactersService: CharactersService

    val viewmodel by lazy { CharacterListViewModel(DataManager(charactersService)) }

    @Test
    fun `init set joke list to empty`() {
        //val jokes = viewmodel.loadCharacters("simpsons viewer")
        val relatedTopic = RelatedTopic()
        relatedTopic.text = "text"
        val icon = Icon()
        icon.url = "url"
        relatedTopic.icon = icon

        `when`(viewmodel.loadCharacters(anyString())).thenReturn(
                just(relatedTopic)
        )
    }
}