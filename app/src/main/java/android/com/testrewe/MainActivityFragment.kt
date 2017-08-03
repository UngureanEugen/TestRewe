package android.com.testrewe

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    lateinit var webView: WebView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        webView = view.findViewById(R.id.webView)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.setAppCacheEnabled(true)
        settings.builtInZoomControls = true


        webView.webViewClient = object : WebViewClient() {
            override fun onLoadResource(view: WebView?, url: String?) {
                Log.e("TAG", "url: " + url)
                super.onLoadResource(view, url)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        CookieSyncManager.createInstance(this@MainActivityFragment.activity)
        val cookieManager = CookieManager.getInstance()
//        cookieManager.removeSessionCookie()
        val cookieString = "MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; optiFirstPageTCXDYF=1; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv_a=1501795155497|int_internal_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; c_lpv=1501795155619|int_internal_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501795155654; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017951534; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A2%2C%22brandSessionCount%22%3A2%2C%22pageViewCountTotal%22%3A4%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22n%2Fa%22%2C%22userCountry%22%3A%22n%2Fa%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22n%2Fa%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017951534=%7B%22firstClickTime%22%3A1501795150%2C%22lastClickTime%22%3A1501795155%2C%22pageViewCount%22%3A2%2C%22sessionDuration%22%3A5%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTUxNTUsImV4cCI6MTUwMTc5NTc1NX0.juY6fF2Yfh4lfpvlNdKkOwNmMEp410kTyRxO0iJxqowyYM2RvlZA-b-mfpEGJJiaBqdNEBbJ_g0zpOB8FdMBiA; s_nr=1501795174278-Repeat; s_invisit=true; MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; optiFirstPageTCXDYF=1; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; c_lpv_a=1501796822008|dir_direct_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv=1501796822404|dir_direct_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501796822563; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017968237; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A3%2C%22brandSessionCount%22%3A3%2C%22pageViewCountTotal%22%3A5%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22nr%22%2C%22userCountry%22%3A%22DE%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22guest%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017968237=%7B%22firstClickTime%22%3A1501796822%2C%22lastClickTime%22%3A1501796822%2C%22pageViewCount%22%3A1%2C%22sessionDuration%22%3A0%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTY4MjIsImV4cCI6MTUwMTc5NzQyMn0.XJin_CxPTOM-svJMO_327DiuNmQN2diC27fgCVrxPZhnsjY20Lm0NrXe_lsmNdtIv0GV4rnhUF3jSYEcRLfS4A; s_nr=1501796880098-Repeat; s_invisit=true"
        cookieManager.setCookie("Cookie", cookieString)
        cookieManager.setCookie("JSESSIONID", "A542339EAB4F2295E72ACE897E87147E.dnaQpt0A")
        cookieManager.setCookie("secureToken", "068c9e73-ea7b-470b-b8c3-d35f3f37eaa7")
        cookieManager.setCookie("rdtc", "sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy")
        cookieManager.setCookie("trbo_usr", "9fd82a5258232ba19135da6e087fdfef")
        cookieManager.setCookie("trbo_session", "3017951534")
        CookieSyncManager.getInstance().sync()


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("https://shop.rewe.de")
                .build()

        val service = retrofit.create<ReweService>(ReweService::class.java)

        service.getPage().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                val map = HashMap<String, String>()
                map.put("X-Requested-With", "XMLHttpRequest")
                map.put("JSESSIONID", "A542339EAB4F2295E72ACE897E87147E.dnaQpt0A")
                map.put("secureToken", "068c9e73-ea7b-470b-b8c3-d35f3f37eaa7")
                map.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                map.put("Cookie", "MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; optiFirstPageTCXDYF=1; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv_a=1501795155497|int_internal_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; c_lpv=1501795155619|int_internal_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501795155654; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017951534; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A2%2C%22brandSessionCount%22%3A2%2C%22pageViewCountTotal%22%3A4%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22n%2Fa%22%2C%22userCountry%22%3A%22n%2Fa%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22n%2Fa%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017951534=%7B%22firstClickTime%22%3A1501795150%2C%22lastClickTime%22%3A1501795155%2C%22pageViewCount%22%3A2%2C%22sessionDuration%22%3A5%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTUxNTUsImV4cCI6MTUwMTc5NTc1NX0.juY6fF2Yfh4lfpvlNdKkOwNmMEp410kTyRxO0iJxqowyYM2RvlZA-b-mfpEGJJiaBqdNEBbJ_g0zpOB8FdMBiA; s_nr=1501795174278-Repeat; s_invisit=true; MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; optiFirstPageTCXDYF=1; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; c_lpv_a=1501796822008|dir_direct_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv=1501796822404|dir_direct_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501796822563; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017968237; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A3%2C%22brandSessionCount%22%3A3%2C%22pageViewCountTotal%22%3A5%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22nr%22%2C%22userCountry%22%3A%22DE%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22guest%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017968237=%7B%22firstClickTime%22%3A1501796822%2C%22lastClickTime%22%3A1501796822%2C%22pageViewCount%22%3A1%2C%22sessionDuration%22%3A0%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTY4MjIsImV4cCI6MTUwMTc5NzQyMn0.XJin_CxPTOM-svJMO_327DiuNmQN2diC27fgCVrxPZhnsjY20Lm0NrXe_lsmNdtIv0GV4rnhUF3jSYEcRLfS4A; s_nr=1501796880098-Repeat; s_invisit=true")

                service.addProduct(map, "editorial-recommendation", "1041138", "1", "068c9e73-ea7b-470b-b8c3-d35f3f37eaa7").enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        val mapWebView = HashMap<String, String>()
                        mapWebView.put("JSESSIONID", "A542339EAB4F2295E72ACE897E87147E.dnaQpt0A")
                        mapWebView.put("secureToken", "068c9e73-ea7b-470b-b8c3-d35f3f37eaa7")
                        mapWebView.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        mapWebView.put("Upgrade-Insecure-Requests", "1")
                        mapWebView.put("Referer", "https://shop.rewe.de/")
                        mapWebView.put("Cookie", "MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; optiFirstPageTCXDYF=1; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv_a=1501795155497|int_internal_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; c_lpv=1501795155619|int_internal_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501795155654; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017951534; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A2%2C%22brandSessionCount%22%3A2%2C%22pageViewCountTotal%22%3A4%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22n%2Fa%22%2C%22userCountry%22%3A%22n%2Fa%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22n%2Fa%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017951534=%7B%22firstClickTime%22%3A1501795150%2C%22lastClickTime%22%3A1501795155%2C%22pageViewCount%22%3A2%2C%22sessionDuration%22%3A5%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTUxNTUsImV4cCI6MTUwMTc5NTc1NX0.juY6fF2Yfh4lfpvlNdKkOwNmMEp410kTyRxO0iJxqowyYM2RvlZA-b-mfpEGJJiaBqdNEBbJ_g0zpOB8FdMBiA; s_nr=1501795174278-Repeat; s_invisit=true; MRefererUrl=no-referer; _rdfa=c2f2dcfc-46c5-40e3-a866-2f024ea6bfb2; mf_2d859e38-92a3-4080-8117-c7e82466e45a=-1; AMCV_65BE20B35350E8DE0A490D45%40AdobeOrg=1999109931%7CMCIDTS%7C17382%7CMCMID%7C76712261490850833281935802638620737155%7CMCAAMLH-1502372038%7C6%7CMCAAMB-1502372038%7CNRX38WO0n5BH8Th-nqAG_A%7CMCAID%7CNONE; s_vi=[CS]v1|2CC199218531436E-4000010C000018E6[CE]; _ga=GA1.2.1143695615.1500386421; _gid=GA1.2.828325737.1501767404; myReweCookie=%7B%22customerZip%22%3A%2251063%22%2C%22serviceText%22%3A%22Liefergebiet+PLZ+51063%22%2C%22serviceType%22%3A%22DELIVERY%22%2C%22deliveryMarketId%22%3A%22231007%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%7D; JSESSIONID=A542339EAB4F2295E72ACE897E87147E.dnaQpt0A; secureToken=068c9e73-ea7b-470b-b8c3-d35f3f37eaa7; rdtc=sxGgRY6XONUT%2FCLNDtl5VQuse-paypal-billing-agreement%2Cpops-include%2Ccheckout-bremen-only%2Cnew-after-sales-page%2Csave-credit-card-data%2Cpd-huerth%2Cuse-checkout-service%2Csave-bank-account-data%2Ctimeslots-via-tss%3BsearchBetaTesters%2Cnew-shop-proxy; s_vnum=1504213200376%26vn%3D3; c_dslv_s=same%20day; optiFirstPageTCXDYF=1; optimizelyEndUserId=oeu1500386420666r0.5274109864967067; c_lpv_a=1501796822008|dir_direct_nn_nn_nn_nn_nn_nn_nn; _uetsid=_uet5fd63548; current_shoppinglist_id=f1fbb771-953f-4a5c-b66a-d088cc04e523; c_lpv=1501796822404|dir_direct_nn_nn_nn_nn_nn_nn_nn; s_adform=rewrewededev; c_dslv=1501796822563; s_cc=true; trbo_usr=9fd82a5258232ba19135da6e087fdfef; trbo_session=3017968237; trbo_us_9fd82a5258232ba19135da6e087fdfef=%7B%22saleCount%22%3A0%2C%22sessionCount%22%3A3%2C%22brandSessionCount%22%3A3%2C%22pageViewCountTotal%22%3A5%2C%22sessionDurationTotal%22%3A78%2C%22externalUserId%22%3A%22%22%2C%22externalData%22%3A%7B%22customerZip%22%3A%2251063%22%2C%22userSC%22%3A%22nr%22%2C%22userCountry%22%3A%22DE%22%2C%22userId%22%3A%22n%2Fa%22%2C%22userType%22%3A%22guest%22%2C%22userStatus%22%3A%22guest%22%2C%22nlSubscriber%22%3A%22n%2Fa%22%2C%22birthYear%22%3A%22n%2Fa%22%2C%22gender%22%3A%22n%2Fa%22%2C%22customerName%22%3A%22n%2Fa%22%2C%22basketIdentification%22%3A%2226ec50df-487a-49be-a048-0102de0eb5a2%22%2C%22couponUsed%22%3A%22no%22%7D%2C%22userCreateTime%22%3A1501789437%7D; trbo_sess_3017968237=%7B%22firstClickTime%22%3A1501796822%2C%22lastClickTime%22%3A1501796822%2C%22pageViewCount%22%3A1%2C%22sessionDuration%22%3A0%7D; rst=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhZXMxOTIiOiJmMGZjYTIxYjk4N2ZjNDYzNzgyZDFkNGViZDNkYmU4MmNkOGNlZTBkZmMwMWE0Mzc0N2EyZDQ4ZTgyNmUwZjg5ZGMyM2RkZDkxNDYxODBmNWRiODkzYjE2YWVjNzZiN2YxZDBmNTU0Njk3OTQ4MTM0MGRjNzQ1N2JkNDhhZjc1YWEyNTgzNTc3MGE4Mjc2NjM5YzQ1MTZhNjMyM2IxOTI0MzY4NGFkNDM2YjY1MjJmNmIyMmRkYTg5NDRhNWE5ZGMzNzYyZjVlMTEwYmExM2EwYzY4YTMzMTU0M2UzNGU2YzRlZjRlOWUyNmMwYTc5YTMzZGUxNGJmOTI1YTZlNWI1ZTRhODA5YTEwZDVjMWI5MzljYTRlZDJmZDhmY2FkM2YiLCJpYXQiOjE1MDE3OTY4MjIsImV4cCI6MTUwMTc5NzQyMn0.XJin_CxPTOM-svJMO_327DiuNmQN2diC27fgCVrxPZhnsjY20Lm0NrXe_lsmNdtIv0GV4rnhUF3jSYEcRLfS4A; s_nr=1501796880098-Repeat; s_invisit=true")

                        //use one
                        webView.loadUrl("https://shop.rewe.de/checkout/start", mapWebView)
                        // or
                        webView.loadUrl("https://shop.rewe.de/checkout/basket", mapWebView)
                    }
                })
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.e("TAG", "onFailure")
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
    }
}
