package jx.pdp_dars.map_july.ui.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jx.pdp_dars.map_july.databinding.ItemHistoryBinding
import jx.pdp_dars.map_july.ui.data.models.local.TravelData

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val data = mutableListOf<TravelData>()
    private var clickListener: ((travelData: TravelData) -> Unit)? = null

    fun setClickListener(travelData: (travelData: TravelData) -> Unit) {
        clickListener = travelData
    }

    fun setTravelList(list: List<TravelData>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: TravelData) {
            binding.travelId.text = data.id
            binding.travelName.text = data.name
            binding.root.setOnClickListener {
                clickListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) =
        holder.bindData(data[position])

    override fun getItemCount() = data.size
}