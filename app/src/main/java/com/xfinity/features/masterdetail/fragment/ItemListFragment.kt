package com.xfinity.features.masterdetail.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.util.LongSparseArray
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xfinity.MVVMApplication
import com.xfinity.R
import com.xfinity.databinding.FragmentItemListBinding
import com.xfinity.features.masterdetail.CharacterListViewModel
import com.xfinity.injection.component.ConfigPersistentComponent
import com.xfinity.injection.component.DaggerConfigPersistentComponent
import com.xfinity.injection.component.FragmentComponent
import com.xfinity.injection.module.FragmentModule
import com.xfinity.util.ViewModelFactory

import java.util.concurrent.atomic.AtomicLong

import javax.inject.Inject

class ItemListFragment : Fragment() {

    private var activityId: Long = 0
    var showIcon: Boolean = false
    private var binding: FragmentItemListBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var characterViewModel: CharacterListViewModel

    private var errorSnackBar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false)
        binding!!.list.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        inject(savedInstanceState)
        return binding!!.root
    }

    private fun inject(savedInstanceState: Bundle?) {
        activityId = savedInstanceState?.getLong(KEY_FRAGMENT_ID) ?: NEXT_ID.getAndIncrement()
        val configPersistentComponent: ConfigPersistentComponent
        if (componentsArray.get(activityId) == null) {
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(MVVMApplication[activity!!.applicationContext].component)
                    .build()
            componentsArray.put(activityId, configPersistentComponent)
        } else {
            configPersistentComponent = componentsArray.get(activityId)
        }

        val fragmentComponent = configPersistentComponent.fragmentComponent(FragmentModule(this))

        fragmentComponent.inject(this)

        characterViewModel = ViewModelProviders.of(this, viewModelFactory).get(CharacterListViewModel::class.java)
        characterViewModel.errorMessage.observe(this, Observer {errorMessage ->
            if (errorMessage != null) {
                showError(errorMessage)
            } else {
                hideError()
            }
        })
        binding!!.viewModel = characterViewModel
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

    private fun showError(errorMessage: Int) {
        errorSnackBar = Snackbar.make(binding!!.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar!!.setAction(R.string.retry, characterViewModel.errorClickListener)
        errorSnackBar!!.show()
    }

    companion object {
        private val KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID"
        private val NEXT_ID = AtomicLong(0)
        private val componentsArray = LongSparseArray<ConfigPersistentComponent>()
        private val QUERY = "query"
    }

}