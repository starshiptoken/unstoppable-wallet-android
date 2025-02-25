package com.starbase.bankwallet.modules.markdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.starbase.bankwallet.core.App
import io.reactivex.Single

object MarkdownModule {

    class Factory(private val markdownUrl: String?, private val gitReleaseUrl: String?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MarkdownViewModel(com.starbase.bankwallet.core.App.connectivityManager, getContentProvider()) as T
        }

        private fun getContentProvider(): IMarkdownContentProvider {
            return when{
                markdownUrl != null -> MarkdownPlainContentProvider(com.starbase.bankwallet.core.App.networkManager, markdownUrl)
                gitReleaseUrl != null -> MarkdownGitReleaseContentProvider(com.starbase.bankwallet.core.App.networkManager, gitReleaseUrl)
                else -> throw IllegalArgumentException()
            }
        }
    }

    interface IMarkdownContentProvider{
        fun getContent(): Single<String>
        var markdownUrl: String
    }
}
