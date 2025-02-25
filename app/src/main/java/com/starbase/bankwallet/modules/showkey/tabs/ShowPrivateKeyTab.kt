package com.starbase.bankwallet.modules.showkey.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseFragment
import io.horizontalsystems.bankwallet.modules.showkey.ShowKeyModule
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_show_private_key_tab.*
import kotlinx.android.synthetic.main.view_holder_private_key.*

class ShowPrivateKeyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_private_key_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val privateKeys = arguments?.getParcelableArrayList<ShowKeyModule.PrivateKey>(PRIVATE_KEYS)
                ?: listOf()

        recyclerView.adapter = PrivateKeysAdapter(privateKeys)
    }

    companion object {
        private const val PRIVATE_KEYS = "privateKeys"

        fun getInstance(privateKeys: List<ShowKeyModule.PrivateKey>): ShowPrivateKeyFragment {
            val fragment = ShowPrivateKeyFragment()

            val arguments = bundleOf()
            arguments.putParcelableArrayList(PRIVATE_KEYS, ArrayList(privateKeys))
            fragment.arguments = arguments
            return fragment
        }
    }
}

class PrivateKeysAdapter(
        private val privateKeys: List<ShowKeyModule.PrivateKey>
) : RecyclerView.Adapter<PrivateKeyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateKeyViewHolder {
        return PrivateKeyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PrivateKeyViewHolder, position: Int) {
        holder.bind(privateKeys[position])
    }

    override fun getItemCount(): Int {
        return privateKeys.size
    }
}

class PrivateKeyViewHolder(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(key: ShowKeyModule.PrivateKey) {
        blockchain.text = key.blockchain
        value.text = key.value
    }

    companion object {
        fun create(parent: ViewGroup): PrivateKeyViewHolder {
            return PrivateKeyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_private_key, parent, false))
        }
    }
}
