package com.lkb.assignment

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        //setting the default currency as INR
        setDefaultCurrency()
        currencyConverter = CurrencyConverter()

        intRecyclerView()

        btnINR.setOnClickListener {
            updateBtnStyle(it as Button)
            changePriceToInr()
        }

        btnAED.setOnClickListener {
            updateBtnStyle(it as Button)
            changePriceToAed()

        }

        btnSAR.setOnClickListener {
            updateBtnStyle(it as Button)
            changePriceToSar()
        }
    }

    override fun onStart() {
        super.onStart()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        loadInitData()
    }


    override fun onDestroy() {
        super.onDestroy()
        DisposableManager.dispose()
    }

    /**
     * Method to initialize the RecyclerView
     */
    private fun intRecyclerView() {
        viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        viewAdapter = MyAdapter()

        (recyclerView as RecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    /*
    Method to set the Default currency
     */
    private fun setDefaultCurrency() {
        //setting default button
        selectedCurrency = btnINR
        selectedCurrency.setTextColor(Color.WHITE)
        selectedCurrency.setBackgroundResource(R.drawable.button_style)
    }

    /**
     * Method to change the price to SAR
     * Long running operation are shifted to the RxJava scheduler
     */
    private fun changePriceToSar() {
        val disposable = productViewModel.refreshProductData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                val d = productViewModel.toSar(r.products, currencyConverter, r.conversion)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { res -> (viewAdapter as MyAdapter).loadData(res) },
                        { err -> Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show() }
                    )
                DisposableManager.add(d)
            }, { err ->
                Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
            })
        DisposableManager.add(disposable)
    }

    /**
     * Method to change the price to AED
     * Long running operation are shifted to the RxJava scheduler
     */
    private fun changePriceToAed() {
        val disposable = productViewModel.refreshProductData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                val d = productViewModel.toAed(r.products, currencyConverter, r.conversion)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { res -> (viewAdapter as MyAdapter).loadData(res) },
                        { err -> Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show() }
                    )
                DisposableManager.add(d)
            }, { err ->
                Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
            })
        DisposableManager.add(disposable)
    }

    /**
     * Method to change the Price to INR
     * Long running operation are shifted to the RxJava scheduler
     */
    private fun changePriceToInr() {
        val disposable = productViewModel.refreshProductData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                val d = productViewModel.toInr(r.products, currencyConverter, r.conversion)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { res -> (viewAdapter as MyAdapter).loadData(res) },
                        { err -> Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show() }
                    )
                DisposableManager.add(d)
            }, { err ->
                Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show()
            })
        DisposableManager.add(disposable)
    }

    /**
     * Method to load the Initial data
     * Long running operation are shifted to the RxJava scheduler
     */
    private fun loadInitData() {
        val disposable = productViewModel.callProductApi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    updateUi(result)
                    val d = productViewModel.toInr(result.products, currencyConverter, result.conversion)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { res -> (viewAdapter as MyAdapter).loadData(res) },
                            { err -> Toast.makeText(this, err.message, Toast.LENGTH_SHORT).show() }
                        )
                    DisposableManager.add(d)
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
        DisposableManager.add(disposable)
    }

    /**
     * Method to set the Title
     */
    private fun updateUi(result: ResponseModel?) {
        tvProductTitle.text = result?.title
    }

    /**
     * Method to update button style
     */
    private fun updateBtnStyle(btn: Button) {
        resetBtnStyle()
        selectedCurrency = btn
        selectedCurrency.setTextColor(Color.WHITE)
        selectedCurrency.setBackgroundResource(R.drawable.button_style)
    }

    /**
     * Reset the button Style
     */
    private fun resetBtnStyle() {
        selectedCurrency.setTextColor(Color.BLACK)
        selectedCurrency.setBackgroundResource(R.drawable.btn_style_normal)
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