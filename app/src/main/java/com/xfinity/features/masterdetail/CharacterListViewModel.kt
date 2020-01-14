package com.xfinity.features.masterdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.SearchView
import android.view.View
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView
import com.xfinity.R
import com.xfinity.data.DataManager
import com.xfinity.data.model.response.RelatedTopic
import com.xfinity.features.masterdetail.data.adapter.ItemListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CharacterListViewModel(private val dataManager: DataManager) : ViewModel() {
    var showIcon: Boolean = false
    private val disposables: CompositeDisposable = CompositeDisposable()
    val responseLiveData: MutableLiveData<List<RelatedTopic>> = MutableLiveData()
    val queryLiveData: MutableLiveData<CharSequence> = MutableLiveData()

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val characterListAdapter = ItemListAdapter()
    val errorClickListener = View.OnClickListener { loadCharacters("the wire characters") }

    init {
        loadCharacters("the wire characters")
    }

    fun loadCharacters(query: String) {
        disposables.add(
                dataManager.getCharacters(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onRetrieveCharacterListStart() }
                        .doAfterTerminate { onRetrieveCharacterListFinish() }
                        .subscribe(
                                { result -> onRetrieveCharacterListSuccess(result as List<RelatedTopic>) },
                                { onRetrievePostListError(it) }
                        )
        )
    }

    fun searchCharacters(searchView: SearchView) {
        RxSearchView.queryTextChanges(searchView)
                .doOnEach { notification ->
                    val query = notification.value as CharSequence
                    queryLiveData.setValue(query)
                }
                .debounce(300, TimeUnit.MILLISECONDS) // to skip intermediate letters
                .retry(3)
                .subscribe()
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun onRetrieveCharacterListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrieveCharacterListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveCharacterListSuccess(postList:List<RelatedTopic>) {
        characterListAdapter.updateCharacterList(postList)
    }

    private fun onRetrievePostListError(throwable: Throwable) {
        throwable.printStackTrace()
        errorMessage.value = R.string.post_error
    }
}