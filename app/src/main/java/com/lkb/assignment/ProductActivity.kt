package com.lkb.assignment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class ProductActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: LinearLayoutManager

    companion object {
        var TAG = "ProductActivity"
    }

    private var disposable: Disposable? = null

    private val lmService by lazy {
        LmService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disposable = lmService.retrieveData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> updateUi(result) },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )

        viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewAdapter = MyAdapter(this)

        (recyclerView as RecyclerView).apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter
            adapter = viewAdapter
        }
    }

    private fun updateUi(result: ResponseModel?) {
        (viewAdapter as MyAdapter).loadData(result?.products)
        tvProductTitle.text = result?.title
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}

class MyAdapter(val context: Context) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    //val ctx = this.context
    private var productData = listOf<Product>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tvItemPrice)
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
        holder.textView.text = productData[position].price
        holder.itemDesc.text = productData[position].name
        ImageLoader.loadImage(holder.itemImage,productData[position].url)
    }

    override fun getItemCount() = productData.size

    fun loadData(data: List<Product>?) {
        this.productData = data!!
        notifyDataSetChanged()
    }
}