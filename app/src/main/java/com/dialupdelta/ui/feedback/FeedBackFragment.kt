package com.dialupdelta.ui.feedback

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentFeedBackBinding
import com.dialupdelta.`interface`.FeedBackItemClickListener
import com.github.mikephil.charting.data.CandleEntry
import org.kodein.di.generic.instance


class FeedBackFragment : BaseFragment(), FeedBackItemClickListener {
  private lateinit var binding :FragmentFeedBackBinding
     private val candleStickChartList:ArrayList<CandleEntry> = ArrayList()
    private val factory: FeedBackViewModelFactory by instance()
    private lateinit var viewModel: FeedBackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_back, container, false)
       return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        viewModel = ViewModelProvider(this, factory)[FeedBackViewModel::class.java]
        setObserver(viewModel)

        binding.webView.webViewClient = MyBrowser()

        binding.webView.getSettings().setLoadsImagesAutomatically(true)
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.loadUrl("https://dialupdelta.com/feedbackData?userId=1")


//        val htmlContent = "<html><body><h1>Hello World!</h1></body></html>"
//        binding.webView.loadData(htmlContent, "text/html", "UTF-8")


//        if(candleStickChartList.size  == 0) {
//            candleStickChartList.add(CandleEntry(0F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(1F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(2F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(3F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(4F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(5F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(6F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(7F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(8F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(9F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(10F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(11F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(12F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(13F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(14F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(15F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(16F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(17F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(18F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(19F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(20F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(21F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(22F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(23F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(24F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(25F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(26F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(27F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(28F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(29F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(30F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(31F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(32F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(33F, 222.95F, 217.27F, 222.15F, 217.88F))
//            candleStickChartList.add(CandleEntry(34F, 225F, 219.84F, 224.94F, 221.07F))
//            candleStickChartList.add(CandleEntry(35F, 228.35F, 222.57F, 223.52F, 226.41F))
//            candleStickChartList.add(CandleEntry(36F, 226.84F, 222.52F, 225.75F, 223.84F))
//            candleStickChartList.add(CandleEntry(37F, 222.95F, 217.27F, 222.15F, 217.88F))
//        }
//        val adapter = FeedbackAdapter(requireActivity(), this, candleStickChartList)
//        binding.feedbackRecyclerView.adapter = adapter
    }

    private fun setObserver(viewModel: FeedBackViewModel) {

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    override fun feedBackItemListener() {
        Intent(context, FeedBackDetailsActivity::class.java).also {
           startActivity(it)
        }
    }

    class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

}