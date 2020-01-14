package com.xfinity.features.masterdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.xfinity.data.model.response.RelatedTopic
import org.greenrobot.eventbus.EventBus

class CharacterViewModel : ViewModel() {
    val characterName = MutableLiveData<String>()
    val characterImage = MutableLiveData<String>()

    fun bind(character: RelatedTopic) {
        characterName.value = character.text
        characterImage.value = character.icon!!.url
    }

    fun openUserDetail() {
        println("_xyz characterName.value = " + characterName.value)
        EventBus.getDefault().post(this)
    }
}