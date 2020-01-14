package com.xfinity.features.masterdetail.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xfinity.MVVMApplication;
import com.xfinity.R;
import com.xfinity.databinding.FragmentItemListBinding;
import com.xfinity.features.masterdetail.CharacterListViewModel;
import com.xfinity.injection.component.ConfigPersistentComponent;
import com.xfinity.injection.component.DaggerConfigPersistentComponent;
import com.xfinity.injection.component.FragmentComponent;
import com.xfinity.injection.module.FragmentModule;
import com.xfinity.util.ViewModelFactory;

import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

public class ItemListFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
    new LongSparseArray<>();
    private static final String QUERY = "query";

    private long activityId;
    public boolean showIcon;
    private FragmentItemListBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    CharacterListViewModel characterViewModel;

    private Snackbar errorSnackBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_list, container, false);
        binding.list.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        inject(savedInstanceState);
        return binding.getRoot();
    }

    private void inject(Bundle savedInstanceState) {
        activityId =
                savedInstanceState != null
        ? savedInstanceState.getLong(KEY_FRAGMENT_ID)
        : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (componentsArray.get(activityId) == null) {
            configPersistentComponent =
                    DaggerConfigPersistentComponent.builder()
                            .appComponent(MVVMApplication.Companion.get(getActivity().getApplicationContext()).getComponent())
                            .build();
            componentsArray.put(activityId, configPersistentComponent);
        } else {
            configPersistentComponent = componentsArray.get(activityId);
        }

        FragmentComponent fragmentComponent =
        configPersistentComponent.fragmentComponent(new FragmentModule(this));

        fragmentComponent.inject(this);

        characterViewModel = ViewModelProviders.of(this, viewModelFactory).get(CharacterListViewModel.class);
        binding.setViewModel(characterViewModel);
    }

    private void hideError() {
        errorSnackBar.dismiss();
    }

    private void showError(int errorMessage) {
        errorSnackBar = Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_INDEFINITE);
        errorSnackBar.setAction(R.string.retry, characterViewModel.getErrorClickListener());
        errorSnackBar.show();
    }

}