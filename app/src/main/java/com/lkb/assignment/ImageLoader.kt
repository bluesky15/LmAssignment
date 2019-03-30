package com.lkb.assignment

import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageLoader{
 companion object {
     fun loadImage(view: ImageView, imageUrl: String) {
         Glide.with(view.context)
             .load(imageUrl)
             .into(view)
     }
 }
}