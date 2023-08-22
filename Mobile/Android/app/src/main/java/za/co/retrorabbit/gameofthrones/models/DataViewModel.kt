package za.co.retrorabbit.gameofthrones.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface IDataViewModel<T> {
    fun onDataChange(data: T)
}

open class DataViewModel<T>(factory: T) : ViewModel(), IDataViewModel<T> {
    private val _data = MutableLiveData(factory)
    var data: LiveData<T> = _data

    override fun onDataChange(data: T) {
        _data.value = data
    }

    fun clear() {
        data = MutableLiveData()
    }
}

interface IDataViewModelList<T> {
    fun onDataChange(data: List<T>)
    fun onDataAdd(data: T)
}

open class DataViewModelList<T> : ViewModel(), IDataViewModelList<T> {
    private val _data = MutableLiveData(emptyList<T>())
    var data: LiveData<List<T>> = _data

    override fun onDataChange(data: List<T>) {
        _data.value = data
    }

    override fun onDataAdd(data: T) {
        _data.value = _data.value?.plus(data)
    }

    fun clear() {
        _data.value = emptyList()
    }
}

class HousesViewModel : DataViewModelList<House>()
class CharacterViewModel : DataViewModel<Person>(Person())
class CharactersViewModel : DataViewModelList<Person>()
class BookViewModel : DataViewModel<Book>(Book())
class BooksViewModel : DataViewModelList<Book>()
