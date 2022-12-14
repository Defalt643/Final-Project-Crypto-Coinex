package com.defalt.cryptocoinex.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.defalt.cryptocoinex.R
import com.defalt.cryptocoinex.databinding.FragmentDetailsBinding
import com.defalt.cryptocoinex.model.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder

class DetailsFragment : Fragment() {

    lateinit var binding: FragmentDetailsBinding
//    lateinit var notificationManager: NotificationManager
//    lateinit var notificationOnChannel: NotificationChannel
//    lateinit var builder: Notification.Builder
    private val channelId = "com.defalt.cryptocoinex.fragment"
    private val notificaionId = 101
    private val description = "Test notification"

    private val item : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val data : CryptoCurrency = item.data!!

        setUpDetail(data)
        loadChart(data)
//        setNotification(data)
        setButtonClick(data)
        addToWatchList(data)

        binding.backStackButton.setOnClickListener {
            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToMarketFragment()
            )
        }

        return binding.root
    }

//    private fun setNotification(data: CryptoCurrency) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val name = data.symbol
//            val descriptionText = data.name
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelId,name,importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager
//                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        }
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            notificationOnChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
////            notificationOnChannel.enableLights(true)
////            notificationOnChannel.lightColor = Color.BLUE
////            notificationOnChannel.enableVibration(false)
////            notificationManager.createNotificationChannel(notificationOnChannel)
////            builder = Notification.Builder(requireContext(),channelId)
////                .setContentTitle(data.symbol)
////                .setContentText(data.name)
////                .setSmallIcon(R.drawable.banner)
////        }
//    }

    var watchList: ArrayList<String>? = null
    var watchListIsChecked = false

    private fun addToWatchList(data: CryptoCurrency) {
        readData()
        watchListIsChecked = if (watchList!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }
        binding.addWatchlistButton.setOnClickListener{
            watchListIsChecked =
                if(!watchListIsChecked){
                    if(!watchList!!.contains(data.symbol)){
                        watchList!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                }else{
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }

    private fun storeData(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist",json)
        editor.apply()
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist",ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json,type)
    }

    private fun setButtonClick(item: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5

        val clickListener = View.OnClickListener {
            when(it.id){
                fifteenMinute.id -> {
                    loadChartData(it, "15", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
                oneHour.id -> {
                    loadChartData(it, "1H", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
                fourHour.id -> {
                    loadChartData(it, "4H", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
                oneDay.id -> {
                    loadChartData(it, "D", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
                oneWeek.id -> {
                    loadChartData(it, "W", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
                oneMonth.id -> {
                    loadChartData(it, "M", item, oneDay, oneMonth, oneWeek, fourHour, oneHour)
                }
            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {

        disableButton(oneDay,oneMonth, oneWeek,fourHour,oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol.toString()
                    + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]"
                    + "&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]"
                    + "&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun disableButton(
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        oneDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null
    }

    private fun loadChart(item: CryptoCurrency) {

        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol.toString()
                    + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]"
                    + "&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]"
                    + "&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpDetail(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = "${String.format("???%.4f",data.quotes[0].price)}"

        if (data.quotes!![0].percentChange24h > 0){
            binding.detailChangeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.2f",data.quotes[0].percentChange24h)} %"
        }else{
            binding.detailChangeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text = "${String.format("%.2f",data.quotes[0].percentChange24h)} %"
        }
    }

}