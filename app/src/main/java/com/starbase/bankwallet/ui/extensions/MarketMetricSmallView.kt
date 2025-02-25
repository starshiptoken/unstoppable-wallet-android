package com.starbase.bankwallet.ui.extensions

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.horizontalsystems.bankwallet.R
import com.starbase.bankwallet.core.App
import io.horizontalsystems.chartview.ChartData
import io.horizontalsystems.views.helpers.LayoutHelper
import kotlinx.android.synthetic.main.view_market_metric_small.view.*
import java.math.BigDecimal

class MarketMetricSmallView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_market_metric_small, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.MarketMetricSmallView)
        try {
            title.text = ta.getString(R.styleable.MarketMetricSmallView_title)
            value.text = ta.getString(R.styleable.MarketMetricSmallView_value)
        } finally {
            ta.recycle()
        }
    }

    fun setMetricData(data: MetricData?) {
        if (data == null) {
            title.alpha = 0.5f
            setValueText(null, false)
            setDiff(null, false)
        } else {
            title.alpha = 1f
            setValueText(data.value, true)
            setDiff(data.diff, true)

            data.chartData?.let {
                post {
                    chart.setData(it)
                }
            }
        }
    }

    private fun setValueText(valueText: String?, modeNotAvailable: Boolean) {
        when {
            valueText != null -> {
                value.text = valueText
                value.setTextColor(context.getColor(R.color.bran))
            }
            modeNotAvailable -> {
                value.text = context.getString(R.string.NotAvailable)
                value.setTextColor(context.getColor(R.color.grey_50))
            }
            else -> {
                value.text = null
            }
        }
    }

    private fun setDiff(diff: BigDecimal?, modeNotAvailable: Boolean) {
        when {
            diff != null -> {
                val sign = if (diff >= BigDecimal.ZERO) "+" else "-"
                diffPercentage.text = com.starbase.bankwallet.core.App.numberFormatter.format(diff.abs(), 0, 2, sign, "%")
                val textColor = if (diff >= BigDecimal.ZERO) R.color.remus else R.color.lucian
                diffPercentage.setTextColor(context.getColor(textColor))
            }
            modeNotAvailable -> {
                diffPercentage.text = "----"
                diffPercentage.setTextColor(context.getColor(R.color.grey_50))
            }
            else -> {
                diffPercentage.text = null
            }
        }
    }
}

data class MetricData(val value: String?, val diff: BigDecimal?, val chartData: ChartData?)
