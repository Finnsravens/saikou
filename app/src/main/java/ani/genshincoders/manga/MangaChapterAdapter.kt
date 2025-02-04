package ani.genshincoders.manga

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ani.genshincoders.databinding.ItemChapterListBinding
import ani.genshincoders.databinding.ItemEpisodeCompactBinding
import ani.genshincoders.media.Media
import ani.genshincoders.setAnimation
import ani.genshincoders.updateAnilistProgress

class MangaChapterAdapter(
    private var type:Int,
    private val media: Media,
    private val fragment: MangaReadFragment,
    var arr: ArrayList<MangaChapter> = arrayListOf(),
):  RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1->ChapterCompactViewHolder(ItemEpisodeCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            0->ChapterListViewHolder(ItemChapterListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else->throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun getItemCount(): Int = arr.size

    inner class ChapterCompactViewHolder(val binding: ItemEpisodeCompactBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                if( 0<=bindingAdapterPosition && bindingAdapterPosition<arr.size)
                    fragment.onMangaChapterClick(arr[bindingAdapterPosition].number)
            }
        }
    }

    inner class ChapterListViewHolder(val binding: ItemChapterListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                if( 0<=bindingAdapterPosition && bindingAdapterPosition<arr.size)
                fragment.onMangaChapterClick(arr[bindingAdapterPosition].number)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChapterCompactViewHolder -> {
                val binding = holder.binding
                setAnimation(fragment.requireContext(),holder.binding.root,fragment.uiSettings)
                val ep = arr[position]
                binding.itemEpisodeNumber.text = ep.number
                if (media.userProgress!=null) {
                    if (ep.number.toFloatOrNull()?:9999f<=media.userProgress!!.toFloat())
                        binding.itemEpisodeViewedCover.visibility=View.VISIBLE
                    else{
                        binding.itemEpisodeViewedCover.visibility=View.GONE
                        binding.itemEpisodeCont.setOnLongClickListener{
                            updateAnilistProgress(media.id, ep.number)
                            true
                        }
                    }
                }
            }
            is ChapterListViewHolder -> {
                val binding = holder.binding
                val ep = arr[position]
                setAnimation(fragment.requireContext(),holder.binding.root,fragment.uiSettings)
                binding.itemChapterNumber.text = ep.number
                binding.itemChapterTitle.text = ep.title
                if (media.userProgress!=null) {
                    if (ep.number.toFloatOrNull()?:9999f<=media.userProgress!!.toFloat()) {
                        binding.itemEpisodeViewedCover.visibility=View.VISIBLE
                        binding.itemEpisodeViewed.visibility = View.VISIBLE
                    } else{
                        binding.itemEpisodeViewedCover.visibility=View.GONE
                        binding.itemEpisodeViewed.visibility = View.GONE
                        binding.root.setOnLongClickListener{
                            updateAnilistProgress(media.id, ep.number)
                            true
                        }
                    }
                }else{
                    binding.itemEpisodeViewedCover.visibility=View.GONE
                    binding.itemEpisodeViewed.visibility = View.GONE
                }
            }
        }
    }
    fun updateType(t:Int){
        type = t
    }
}
