package com.example.mindjourney.components

import ThoughtFormDao
import ThoughtFormEntity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ThoughtFormViewModel(private val thoughtFormDao: ThoughtFormDao) : ViewModel() {

    fun getUserForms(userUid: String): Flow<List<ThoughtFormEntity>> {
        return thoughtFormDao.getUserForms(userUid)
    }

    fun saveForm(form: ThoughtFormEntity) {
        viewModelScope.launch {
            thoughtFormDao.insert(form)
        }
    }

    fun deleteForm(form: ThoughtFormEntity) {
        viewModelScope.launch {
            thoughtFormDao.delete(form)
        }
    }
}

