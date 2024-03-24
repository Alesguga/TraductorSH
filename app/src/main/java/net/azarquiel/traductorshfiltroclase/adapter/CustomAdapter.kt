package net.azarquiel.recyclerviewWords.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.azarquiel.traductorshfiltroclase.R
import net.azarquiel.traductorshfiltroclase.model.Word

/**
 * Created by pacopulido on 9/10/18.
 */
class CustomAdapter(val context: Context,
                    val layout: Int,
                    val listener: OnClickListenerRecycler
                    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var dataList: List<Word> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewlayout = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(viewlayout, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    internal fun setWords(words: List<Word>) {
        this.dataList = words
        notifyDataSetChanged()
    }


    class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
        fun bind(dataItem: Word, listener: OnClickListenerRecycler){
            // itemview es el item de diseño
            // al que hay que poner los datos del objeto dataItem
            val tvsprow = itemView.findViewById(R.id.tvsprow) as TextView
            val tvenrow = itemView.findViewById(R.id.tvenrow) as TextView
            val ivspeakersp = itemView.findViewById(R.id.ivspeakersp) as ImageView
            val ivspeakeren = itemView.findViewById(R.id.ivspeakeren) as ImageView

            tvenrow.text = dataItem.enWord
            tvsprow.text = dataItem.spWord
            ivspeakersp.setOnClickListener { listener.onClickSp(itemView) }
            ivspeakeren.setOnClickListener { listener.onClickEn(itemView) }
            itemView.tag = dataItem
        }
    }
    interface OnClickListenerRecycler {
        fun onClickSp(itemView: View) {

        }
        fun onClickEn(itemView: View) {

        }
    }
}