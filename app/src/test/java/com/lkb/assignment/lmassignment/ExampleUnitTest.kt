package com.lkb.assignment.lmassignment

import com.lkb.assignment.ProductActivity
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ProductActivityTest{
    @Test
    fun checking_activity_is_not_null() {
        val activity = Robolectric.setupActivity(ProductActivity::class.java!!)
       assertTrue(activity!=null)
    }

}
