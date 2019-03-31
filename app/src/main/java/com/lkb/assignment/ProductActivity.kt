package com.lkb.assignment

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lkb.assignment.model.Product
import com.lkb.assignment.model.ResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class ProductActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var productViewModel: ProductViewModel
    private lateinit var selectedCurrency: Button
    private lateinit var currencyConverter: CurrencyConverter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setting default button
        selectedCurrency = btnINR
        selectedCurrency.setTextColor(Color.WHITE)
        selectedCurrency.setBackgroundResource(R.drawable.button_style)

        currencyConverter = CurrencyConverter()

        viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewAdapter = MyAdapter(selectedCurrency.text.toString())

        (recyclerView as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        btnINR.setOnClickListener {
            updateBtnStyle(it as Button)
            val disposable = productViewModel.refreshProductData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { r ->
                    currencyConverter.updateCurrRate(r.conversion)
                    toInr(r.products)
                }
            DisposableManager.add(disposable)
        }
        btnAED.setOnClickListener {
            updateBtnStyle(it as Button)
            val disposable = productViewModel.refreshProductData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { r ->
                    currencyConverter.updateCurrRate(r.conversion)
                    toAed(r.products)
                }
            DisposableManager.add(disposable)

        }
        btnSAR.setOnClickListener {
            updateBtnStyle(it as Button)
            val disposable = productViewModel.refreshProductData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { r ->
                    currencyConverter.updateCurrRate(r.conversion)
                    toSar(r.products)
                }
            DisposableManager.add(disposable)
        }
    }

    override fun onStart() {
        super.onStart()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        val disposable = productViewModel.callProductApi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    updateUi(result)
                    toInr(result.products)
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
        DisposableManager.add(disposable)
    }

    private fun updateUi(result: ResponseModel?) {
        tvProductTitle.text = result?.title
    }

    override fun onDestroy() {
        super.onDestroy()
        DisposableManager.dispose()
    }

    private fun updateBtnStyle(btn: Button) {
        resetBtnStyle()
        selectedCurrency = btn
        selectedCurrency.setTextColor(Color.WHITE)
        selectedCurrency.setBackgroundResource(R.drawable.button_style)
    }

    private fun resetBtnStyle() {
        selectedCurrency.setTextColor(Color.BLACK)
        selectedCurrency.setBackgroundResource(R.drawable.btn_style_normal)
    }


    private fun toSar(pdata: List<Product>) {
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("INR")) {
                pdata[index].price = currencyConverter.convertINRtoSAR(pdata[index].price.toDouble())!!
                pdata[index].currency = "SAR"
            } else if (pdata[index].currency.contains("AED")) {
                pdata[index].price = currencyConverter.convertAEDtoSAR(pdata[index].price.toDouble())!!
                pdata[index].currency = "SAR"
            }
        }
        (viewAdapter as MyAdapter).loadData(pdata)
    }

    private fun toAed(pdata: List<Product>) {
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("INR")) {
                pdata[index].price = currencyConverter.convertINRtoAED(pdata[index].price.toDouble())!!
                pdata[index].currency = "AED"
            } else if (pdata[index].currency.contains("SAR")) {
                pdata[index].price = currencyConverter.convertSARtoAED(pdata[index].price.toDouble())!!
                pdata[index].currency = "AED"
            }
        }
        (viewAdapter as MyAdapter).loadData(pdata)
    }

    private fun toInr(pdata: List<Product>) {
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("AED")) {
                pdata[index].price = currencyConverter.convertAEDtoINR(pdata[index].price.toDouble())!!
                pdata[index].currency = "INR"
            } else if (pdata[index].currency.contains("SAR")) {
                pdata[index].price = currencyConverter.convertSARtoINR(pdata[index].price.toDouble())!!
                pdata[index].currency = "INR"
            }
        }
        (viewAdapter as MyAdapter).loadData(pdata)
    }

}

class MyAdapter(val selectedCurr: String) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
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

/**
 * This is the disposable manager for the RxJava disposable
 * It stores all the disposables and
 * dispose them  on onDestroy of Activity
 */
object DisposableManager {

    private var compositeDisposable: CompositeDisposable? = null

    fun add(disposable: Disposable) {
        getCompositeDisposable().add(disposable)
    }

    fun dispose() {
        getCompositeDisposable().dispose()
    }

    private fun getCompositeDisposable(): CompositeDisposable {
        if (compositeDisposable == null || compositeDisposable!!.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        return compositeDisposable as CompositeDisposable
    }
}