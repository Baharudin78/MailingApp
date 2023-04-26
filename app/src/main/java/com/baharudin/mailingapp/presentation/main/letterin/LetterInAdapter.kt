package com.baharudin.mailingapp.presentation.main.letterin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baharudin.mailingapp.databinding.ItemLetterBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.bumptech.glide.Glide

class LetterInAdapter(
    private val letter : MutableList<LetterEntity>
) : RecyclerView.Adapter<LetterInAdapter.LetterViewHolder>(){

    interface OnItemTap {
        fun onTap(product: LetterEntity)
    }

    fun setItemTapListener(l: OnItemTap){
        onTapListener = l
    }

    private var onTapListener: OnItemTap? = null

    fun updateList(mProducts: List<LetterEntity>){
        letter.clear()
        letter.addAll(mProducts)
        notifyDataSetChanged()
    }

    inner class LetterViewHolder(private val itemBinding: ItemLetterBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(letter: LetterEntity){
            if (letter.letterKinds == "letter_in"){
                itemBinding.tvName.text = "Surat Masuk"
            }else{
                itemBinding.tvName.text = "Surat Keluar"
            }
            itemBinding.tvAlias.text = letter.letterDate

            Glide.with(itemBinding.root.context)
                .load(letter.imagesLetterUrl)
                .into(itemBinding.ivPhoto)
            itemBinding.root.setOnClickListener {
                onTapListener?.onTap(letter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val inflater = ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LetterViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(letter[position])
    }

    override fun getItemCount(): Int {
        return letter.size
    }
}