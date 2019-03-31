package com.lkb.assignment

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lkb.assignment.model.Product

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var productData = listOf<Product>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val itemDesc: TextView = view.findViewById(R.id.tvItemDesc)
        val itemImage: ImageView = view.findViewById(R.id.ivItemImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyAdapter.MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (productData[position].currency.contains("INR")) {
            holder.itemPrice.text = Html.fromHtml(
                "&#x20B9; " +
                        productData[position].price
                , Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            holder.itemPrice.text = Html.fromHtml(
                productData[position].currency + " " +
                        productData[position].price
                , Html.FROM_HTML_MODE_LEGACY
            )
        }

        holder.itemDesc.text = productData[position].name
        ImageLoader.loadImage(holder.itemImage, productData[position].url)
    }

    override fun getItemCount() = productData.size

    fun loadData(data: List<Product>?) {
        this.productData = data!!
        notifyDataSetChanged()
    }

}