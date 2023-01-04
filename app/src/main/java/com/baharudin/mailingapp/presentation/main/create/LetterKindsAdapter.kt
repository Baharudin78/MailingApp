package com.baharudin.mailingapp.presentation.main.create

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ItemLetterKindsBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterKindEntity
import com.baharudin.mailingapp.presentation.common.extention.withDelay

class LetterKindsAdapter(
    private val letterKinds : List<LetterKindEntity>
    ) : RecyclerView.Adapter<LetterKindsAdapter.LetterKindHolder>(){

    private var selectedPosition = -1

    interface OnItemClickListener{
        fun onClick(letterKinds: LetterKindEntity)
    }

    fun setItemClickListener(clickInterface : OnItemClickListener) {
        onClickListener = clickInterface
    }

    private var onClickListener : OnItemClickListener? = null

    inner class LetterKindHolder(
        val binding : ItemLetterKindsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun binItem(item : LetterKindEntity){
            binding.apply {
                binding.tvLetter.text = item.name
                binding.tvLetterAdditional.text = item.nameAditional
            }
            if (item.selected){
                binding.layoutCard.background =
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.border_selected
                    )
                binding.tvLetter.setTextColor(Color.WHITE)
            }
            binding.root.setOnClickListener {
                onClickListener?.onClick(item)
                selectItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterKindHolder {
        val inflater = ItemLetterKindsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LetterKindHolder(inflater)
    }

    override fun onBindViewHolder(holder: LetterKindHolder, position: Int) {
        holder.binItem(letterKinds[position])
    }

    override fun getItemCount(): Int {
        return letterKinds.size
    }

    private fun selectItem(position: Int) {
        if(position != selectedPosition) {
            if(selectedPosition > -1) {
                letterKinds[selectedPosition].selected = false
                notifyItemChanged(selectedPosition)
            }

            selectedPosition = position
            letterKinds[position].selected = true

            withDelay {
                notifyItemChanged(position)
            }
        }
    }
}