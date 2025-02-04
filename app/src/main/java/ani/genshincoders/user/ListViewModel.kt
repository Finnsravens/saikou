package ani.genshincoders.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ani.genshincoders.anilist.Anilist
import ani.genshincoders.loadData
import ani.genshincoders.media.Media

class ListViewModel : ViewModel() {
    var grid = MutableLiveData(loadData<Boolean>("listGrid")?:true)

    private val lists = MutableLiveData<MutableMap<String,ArrayList<Media>>>()
    fun getLists() : LiveData<MutableMap<String,ArrayList<Media>>> = lists
    fun loadLists(anime:Boolean,userId:Int) {
        lists.postValue(Anilist.query.getMediaLists(anime,userId))
    }
}