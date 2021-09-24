package com.starbase.bankwallet.modules.launcher

import android.content.Context
import android.content.Intent
import com.starbase.bankwallet.core.App
import io.horizontalsystems.core.security.KeyStoreValidationResult

object LaunchModule {

    interface IView

    interface IViewDelegate {
        fun viewDidLoad()
        fun didUnlock()
        fun didCancelUnlock()
    }

    interface IInteractor {
        val isPinNotSet: Boolean
        val isAccountsEmpty: Boolean
        val isSystemLockOff: Boolean
        val mainShowedOnce: Boolean

        fun validateKeyStore(): KeyStoreValidationResult
    }

    interface IInteractorDelegate

    interface IRouter {
        fun openWelcomeModule()
        fun openMainModule()
        fun openUnlockModule()
        fun closeApplication()
        fun openNoSystemLockModule()
        fun openKeyInvalidatedModule()
        fun openUserAuthenticationModule()
    }

    fun init(view: LaunchViewModel, router: IRouter) {
        val interactor = LaunchInteractor(com.starbase.bankwallet.core.App.accountManager, com.starbase.bankwallet.core.App.pinComponent, com.starbase.bankwallet.core.App.systemInfoManager, com.starbase.bankwallet.core.App.keyStoreManager, com.starbase.bankwallet.core.App.localStorage)
        val presenter = LaunchPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

    fun start(context: Context) {
        val intent = Intent(context, LauncherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

}
