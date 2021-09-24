package com.starbase.bankwallet.core

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.starbase.bankwallet.BuildConfig
import io.horizontalsystems.bankwallet.core.factories.AccountFactory
import io.horizontalsystems.bankwallet.core.factories.AdapterFactory
import io.horizontalsystems.bankwallet.core.factories.AddressParserFactory
import io.horizontalsystems.bankwallet.core.managers.*
import io.horizontalsystems.bankwallet.core.notifications.NotificationNetworkWrapper
import io.horizontalsystems.bankwallet.core.notifications.NotificationWorker
import io.horizontalsystems.bankwallet.core.providers.AppConfigProvider
import io.horizontalsystems.bankwallet.core.providers.FeeCoinProvider
import io.horizontalsystems.bankwallet.core.providers.FeeRateProvider
import io.horizontalsystems.bankwallet.core.storage.*
import io.horizontalsystems.bankwallet.modules.keystore.KeyStoreActivity
import io.horizontalsystems.bankwallet.modules.launcher.LauncherActivity
import io.horizontalsystems.bankwallet.modules.lockscreen.LockScreenActivity
import io.horizontalsystems.bankwallet.modules.settings.theme.ThemeType
import io.horizontalsystems.bankwallet.modules.tor.TorConnectionActivity
import io.horizontalsystems.bankwallet.modules.walletconnect.WalletConnectManager
import io.horizontalsystems.bankwallet.modules.walletconnect.WalletConnectRequestManager
import io.horizontalsystems.bankwallet.modules.walletconnect.WalletConnectSessionManager
import io.horizontalsystems.coinkit.CoinKit
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.CoreApp
import io.horizontalsystems.core.ICoreApp
import io.horizontalsystems.core.security.EncryptionManager
import io.horizontalsystems.core.security.KeyStoreManager
import io.horizontalsystems.ethereumkit.core.EthereumKit
import io.horizontalsystems.pin.PinComponent
import io.reactivex.plugins.RxJavaPlugins
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class App : CoreApp() {

    companion object : ICoreApp by CoreApp {

        lateinit var feeRateProvider: FeeRateProvider
        lateinit var localStorage: com.starbase.bankwallet.core.ILocalStorage
        lateinit var marketStorage: com.starbase.bankwallet.core.IMarketStorage
        lateinit var torKitManager: com.starbase.bankwallet.core.ITorManager
        lateinit var chartTypeStorage: com.starbase.bankwallet.core.IChartTypeStorage
        lateinit var restoreSettingsStorage: com.starbase.bankwallet.core.IRestoreSettingsStorage

        lateinit var wordsManager: WordsManager
        lateinit var networkManager: com.starbase.bankwallet.core.INetworkManager
        lateinit var backgroundStateChangeListener: BackgroundStateChangeListener
        lateinit var appConfigProvider: com.starbase.bankwallet.core.IAppConfigProvider
        lateinit var adapterManager: com.starbase.bankwallet.core.IAdapterManager
        lateinit var walletManager: com.starbase.bankwallet.core.IWalletManager
        lateinit var walletStorage: com.starbase.bankwallet.core.IWalletStorage
        lateinit var accountManager: com.starbase.bankwallet.core.IAccountManager
        lateinit var accountFactory: com.starbase.bankwallet.core.IAccountFactory
        lateinit var backupManager: com.starbase.bankwallet.core.IBackupManager

        lateinit var xRateManager: com.starbase.bankwallet.core.IRateManager
        lateinit var connectivityManager: ConnectivityManager
        lateinit var appDatabase: AppDatabase
        lateinit var accountsStorage: com.starbase.bankwallet.core.IAccountsStorage
        lateinit var priceAlertManager: com.starbase.bankwallet.core.IPriceAlertManager
        lateinit var enabledWalletsStorage: com.starbase.bankwallet.core.IEnabledWalletStorage
        lateinit var blockchainSettingsStorage: com.starbase.bankwallet.core.IBlockchainSettingsStorage
        lateinit var ethereumKitManager: EvmKitManager
        lateinit var binanceSmartChainKitManager: EvmKitManager
        lateinit var binanceKitManager: BinanceKitManager
        lateinit var numberFormatter: com.starbase.bankwallet.core.IAppNumberFormatter
        lateinit var addressParserFactory: AddressParserFactory
        lateinit var feeCoinProvider: FeeCoinProvider
        lateinit var notificationNetworkWrapper: NotificationNetworkWrapper
        lateinit var notificationManager: com.starbase.bankwallet.core.INotificationManager
        lateinit var ethereumRpcModeSettingsManager: com.starbase.bankwallet.core.IEthereumRpcModeSettingsManager
        lateinit var initialSyncModeSettingsManager: com.starbase.bankwallet.core.IInitialSyncModeSettingsManager
        lateinit var derivationSettingsManager: com.starbase.bankwallet.core.IDerivationSettingsManager
        lateinit var bitcoinCashCoinTypeManager: BitcoinCashCoinTypeManager
        lateinit var accountCleaner: com.starbase.bankwallet.core.IAccountCleaner
        lateinit var rateAppManager: com.starbase.bankwallet.core.IRateAppManager
        lateinit var coinManager: com.starbase.bankwallet.core.ICoinManager
        lateinit var walletConnectSessionStorage: WalletConnectSessionStorage
        lateinit var walletConnectSessionManager: WalletConnectSessionManager
        lateinit var walletConnectRequestManager: WalletConnectRequestManager
        lateinit var walletConnectManager: WalletConnectManager
        lateinit var notificationSubscriptionManager: com.starbase.bankwallet.core.INotificationSubscriptionManager
        lateinit var termsManager: com.starbase.bankwallet.core.ITermsManager
        lateinit var marketFavoritesManager: MarketFavoritesManager
        lateinit var coinKit: CoinKit
        lateinit var activateCoinManager: ActivateCoinManager
        lateinit var releaseNotesManager: ReleaseNotesManager
        lateinit var restoreSettingsManager: RestoreSettingsManager
        lateinit var evmNetworkManager: EvmNetworkManager
        lateinit var accountSettingManager: AccountSettingManager
    }

    override fun onCreate() {
        super.onCreate()

        if (!com.starbase.bankwallet.BuildConfig.DEBUG) {
            //Disable logging for lower levels in Release build
            Logger.getLogger("").level = Level.SEVERE
        }

        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            Log.w("RxJava ErrorHandler", e)
        }

        EthereumKit.init()

        instance = this
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val appConfig = AppConfigProvider()
        com.starbase.bankwallet.core.App.Companion.appConfigProvider = appConfig
        buildConfigProvider = appConfig
        languageConfigProvider = appConfig

        com.starbase.bankwallet.core.App.Companion.coinKit = CoinKit.create(this, buildConfigProvider.testMode)

        com.starbase.bankwallet.core.App.Companion.feeRateProvider = FeeRateProvider(com.starbase.bankwallet.core.App.Companion.appConfigProvider)
        backgroundManager = BackgroundManager(this)

        com.starbase.bankwallet.core.App.Companion.appDatabase = AppDatabase.getInstance(this)

        com.starbase.bankwallet.core.App.Companion.evmNetworkManager = EvmNetworkManager(com.starbase.bankwallet.core.App.Companion.appConfigProvider)
        com.starbase.bankwallet.core.App.Companion.accountSettingManager = AccountSettingManager(AccountSettingRecordStorage(
            com.starbase.bankwallet.core.App.Companion.appDatabase
        ), com.starbase.bankwallet.core.App.Companion.evmNetworkManager
        )

        com.starbase.bankwallet.core.App.Companion.ethereumKitManager = EvmKitManager(appConfig.etherscanApiKey, backgroundManager, EvmNetworkProviderEth(
            com.starbase.bankwallet.core.App.Companion.accountSettingManager
        ))
        com.starbase.bankwallet.core.App.Companion.binanceSmartChainKitManager = EvmKitManager(appConfig.bscscanApiKey, backgroundManager, EvmNetworkProviderBsc(
            com.starbase.bankwallet.core.App.Companion.accountSettingManager
        ))
        com.starbase.bankwallet.core.App.Companion.binanceKitManager = BinanceKitManager(buildConfigProvider.testMode)

        com.starbase.bankwallet.core.App.Companion.accountsStorage = AccountsStorage(com.starbase.bankwallet.core.App.Companion.appDatabase)
        com.starbase.bankwallet.core.App.Companion.restoreSettingsStorage = RestoreSettingsStorage(
            com.starbase.bankwallet.core.App.Companion.appDatabase
        )

        com.starbase.bankwallet.core.AppLog.logsDao = com.starbase.bankwallet.core.App.Companion.appDatabase.logsDao()

        com.starbase.bankwallet.core.App.Companion.coinManager = CoinManager(
            com.starbase.bankwallet.core.App.Companion.coinKit,
            com.starbase.bankwallet.core.App.Companion.appConfigProvider
        )

        com.starbase.bankwallet.core.App.Companion.enabledWalletsStorage = EnabledWalletsStorage(com.starbase.bankwallet.core.App.Companion.appDatabase)
        com.starbase.bankwallet.core.App.Companion.blockchainSettingsStorage = BlockchainSettingsStorage(
            com.starbase.bankwallet.core.App.Companion.appDatabase
        )
        com.starbase.bankwallet.core.App.Companion.walletStorage = WalletStorage(
            com.starbase.bankwallet.core.App.Companion.coinManager,
            com.starbase.bankwallet.core.App.Companion.enabledWalletsStorage
        )

        LocalStorageManager(preferences).apply {
            com.starbase.bankwallet.core.App.Companion.localStorage = this
            com.starbase.bankwallet.core.App.Companion.chartTypeStorage = this
            pinStorage = this
            thirdKeyboardStorage = this
            com.starbase.bankwallet.core.App.Companion.marketStorage = this
        }

        com.starbase.bankwallet.core.App.Companion.torKitManager = TorManager(instance,
            com.starbase.bankwallet.core.App.Companion.localStorage
        )

        com.starbase.bankwallet.core.App.Companion.wordsManager = WordsManager()
        com.starbase.bankwallet.core.App.Companion.networkManager = NetworkManager()
        com.starbase.bankwallet.core.App.Companion.accountCleaner = AccountCleaner(buildConfigProvider.testMode)
        com.starbase.bankwallet.core.App.Companion.accountManager = AccountManager(
            com.starbase.bankwallet.core.App.Companion.accountsStorage,
            com.starbase.bankwallet.core.App.Companion.accountCleaner
        )
        com.starbase.bankwallet.core.App.Companion.accountFactory = AccountFactory(com.starbase.bankwallet.core.App.Companion.accountManager)
        com.starbase.bankwallet.core.App.Companion.backupManager = BackupManager(com.starbase.bankwallet.core.App.Companion.accountManager)
        com.starbase.bankwallet.core.App.Companion.walletManager = WalletManager(
            com.starbase.bankwallet.core.App.Companion.accountManager,
            com.starbase.bankwallet.core.App.Companion.walletStorage
        )

        KeyStoreManager("MASTER_KEY", KeyStoreCleaner(
            com.starbase.bankwallet.core.App.Companion.localStorage,
            com.starbase.bankwallet.core.App.Companion.accountManager,
            com.starbase.bankwallet.core.App.Companion.walletManager
        )).apply {
            keyStoreManager = this
            keyProvider = this
        }

        encryptionManager = EncryptionManager(keyProvider)

        systemInfoManager = SystemInfoManager()

        languageManager = LanguageManager()
        currencyManager = CurrencyManager(
            com.starbase.bankwallet.core.App.Companion.localStorage,
            com.starbase.bankwallet.core.App.Companion.appConfigProvider
        )
        com.starbase.bankwallet.core.App.Companion.numberFormatter = NumberFormatter(languageManager)

        com.starbase.bankwallet.core.App.Companion.connectivityManager = ConnectivityManager(backgroundManager)

        val zcashBirthdayProvider = ZcashBirthdayProvider(this, buildConfigProvider.testMode)
        com.starbase.bankwallet.core.App.Companion.restoreSettingsManager = RestoreSettingsManager(
            com.starbase.bankwallet.core.App.Companion.restoreSettingsStorage, zcashBirthdayProvider)

        val adapterFactory = AdapterFactory(instance, buildConfigProvider.testMode,
            com.starbase.bankwallet.core.App.Companion.ethereumKitManager,
            com.starbase.bankwallet.core.App.Companion.binanceSmartChainKitManager,
            com.starbase.bankwallet.core.App.Companion.binanceKitManager, backgroundManager,
            com.starbase.bankwallet.core.App.Companion.restoreSettingsManager,
            com.starbase.bankwallet.core.App.Companion.coinManager
        )
        com.starbase.bankwallet.core.App.Companion.adapterManager = AdapterManager(
            com.starbase.bankwallet.core.App.Companion.walletManager, adapterFactory,
            com.starbase.bankwallet.core.App.Companion.ethereumKitManager,
            com.starbase.bankwallet.core.App.Companion.binanceSmartChainKitManager,
            com.starbase.bankwallet.core.App.Companion.binanceKitManager
        )

        com.starbase.bankwallet.core.App.Companion.initialSyncModeSettingsManager = InitialSyncSettingsManager(
            com.starbase.bankwallet.core.App.Companion.coinManager,
            com.starbase.bankwallet.core.App.Companion.blockchainSettingsStorage,
            com.starbase.bankwallet.core.App.Companion.adapterManager,
            com.starbase.bankwallet.core.App.Companion.walletManager
        )
        com.starbase.bankwallet.core.App.Companion.derivationSettingsManager = DerivationSettingsManager(
            com.starbase.bankwallet.core.App.Companion.blockchainSettingsStorage,
            com.starbase.bankwallet.core.App.Companion.adapterManager,
            com.starbase.bankwallet.core.App.Companion.walletManager
        )
        com.starbase.bankwallet.core.App.Companion.ethereumRpcModeSettingsManager = EthereumRpcModeSettingsManager(
            com.starbase.bankwallet.core.App.Companion.blockchainSettingsStorage,
            com.starbase.bankwallet.core.App.Companion.adapterManager,
            com.starbase.bankwallet.core.App.Companion.walletManager
        )
        com.starbase.bankwallet.core.App.Companion.bitcoinCashCoinTypeManager = BitcoinCashCoinTypeManager(
            com.starbase.bankwallet.core.App.Companion.walletManager,
            com.starbase.bankwallet.core.App.Companion.adapterManager,
            com.starbase.bankwallet.core.App.Companion.blockchainSettingsStorage
        )

        adapterFactory.initialSyncModeSettingsManager =
            com.starbase.bankwallet.core.App.Companion.initialSyncModeSettingsManager
        adapterFactory.ethereumRpcModeSettingsManager =
            com.starbase.bankwallet.core.App.Companion.ethereumRpcModeSettingsManager

        com.starbase.bankwallet.core.App.Companion.feeCoinProvider = FeeCoinProvider(com.starbase.bankwallet.core.App.Companion.coinKit)
        com.starbase.bankwallet.core.App.Companion.xRateManager = RateManager(this,
            com.starbase.bankwallet.core.App.Companion.appConfigProvider
        )

        com.starbase.bankwallet.core.App.Companion.addressParserFactory = AddressParserFactory()

        com.starbase.bankwallet.core.App.Companion.notificationNetworkWrapper = NotificationNetworkWrapper(
            com.starbase.bankwallet.core.App.Companion.localStorage,
            com.starbase.bankwallet.core.App.Companion.networkManager,
            com.starbase.bankwallet.core.App.Companion.appConfigProvider
        )
        com.starbase.bankwallet.core.App.Companion.notificationManager = NotificationManager(NotificationManagerCompat.from(this),
            com.starbase.bankwallet.core.App.Companion.localStorage
        ).apply {
            backgroundManager.registerListener(this)
        }
        com.starbase.bankwallet.core.App.Companion.notificationSubscriptionManager = NotificationSubscriptionManager(
            com.starbase.bankwallet.core.App.Companion.appDatabase,
            com.starbase.bankwallet.core.App.Companion.notificationNetworkWrapper
        )
        com.starbase.bankwallet.core.App.Companion.priceAlertManager = PriceAlertManager(
            com.starbase.bankwallet.core.App.Companion.appDatabase,
            com.starbase.bankwallet.core.App.Companion.notificationSubscriptionManager,
            com.starbase.bankwallet.core.App.Companion.notificationManager,
            com.starbase.bankwallet.core.App.Companion.localStorage,
            com.starbase.bankwallet.core.App.Companion.notificationNetworkWrapper, backgroundManager)

        pinComponent = PinComponent(
                pinStorage = pinStorage,
                encryptionManager = encryptionManager,
                excludedActivityNames = listOf(
                        KeyStoreActivity::class.java.name,
                        LockScreenActivity::class.java.name,
                        LauncherActivity::class.java.name,
                        TorConnectionActivity::class.java.name
                )
        )

        com.starbase.bankwallet.core.App.Companion.backgroundStateChangeListener = BackgroundStateChangeListener(systemInfoManager, keyStoreManager, pinComponent).apply {
            backgroundManager.registerListener(this)
        }

        com.starbase.bankwallet.core.App.Companion.rateAppManager = RateAppManager(
            com.starbase.bankwallet.core.App.Companion.walletManager,
            com.starbase.bankwallet.core.App.Companion.adapterManager,
            com.starbase.bankwallet.core.App.Companion.localStorage
        )
        com.starbase.bankwallet.core.App.Companion.walletConnectSessionStorage = WalletConnectSessionStorage(
            com.starbase.bankwallet.core.App.Companion.appDatabase
        )
        com.starbase.bankwallet.core.App.Companion.walletConnectSessionManager = WalletConnectSessionManager(
            com.starbase.bankwallet.core.App.Companion.walletConnectSessionStorage,
            com.starbase.bankwallet.core.App.Companion.accountManager,
            com.starbase.bankwallet.core.App.Companion.accountSettingManager
        )
        com.starbase.bankwallet.core.App.Companion.walletConnectRequestManager = WalletConnectRequestManager()
        com.starbase.bankwallet.core.App.Companion.walletConnectManager = WalletConnectManager(
            com.starbase.bankwallet.core.App.Companion.accountManager,
            com.starbase.bankwallet.core.App.Companion.ethereumKitManager,
            com.starbase.bankwallet.core.App.Companion.binanceSmartChainKitManager
        )

        com.starbase.bankwallet.core.App.Companion.termsManager = TermsManager(com.starbase.bankwallet.core.App.Companion.localStorage)

        com.starbase.bankwallet.core.App.Companion.marketFavoritesManager = MarketFavoritesManager(
            com.starbase.bankwallet.core.App.Companion.appDatabase
        )

        com.starbase.bankwallet.core.App.Companion.activateCoinManager = ActivateCoinManager(
            com.starbase.bankwallet.core.App.Companion.coinKit,
            com.starbase.bankwallet.core.App.Companion.walletManager,
            com.starbase.bankwallet.core.App.Companion.accountManager
        )

        com.starbase.bankwallet.core.App.Companion.releaseNotesManager = ReleaseNotesManager(systemInfoManager,
            com.starbase.bankwallet.core.App.Companion.localStorage,
            com.starbase.bankwallet.core.App.Companion.appConfigProvider
        )

        setAppTheme()

        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks(com.starbase.bankwallet.core.App.Companion.torKitManager))

        startTasks()

        NotificationWorker.startPeriodicWorker(instance)
    }

    private fun setAppTheme() {
        val nightMode = when (com.starbase.bankwallet.core.App.Companion.localStorage.currentTheme) {
            ThemeType.Light -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeType.Dark -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeType.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            TRIM_MEMORY_BACKGROUND,
            TRIM_MEMORY_MODERATE,
            TRIM_MEMORY_COMPLETE -> {
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                if (backgroundManager.inBackground) {
                    val logger = com.starbase.bankwallet.core.AppLogger("low memory")
                    logger.info("Kill app due to low memory, level: $level")
                    exitProcess(0)
                }
            }
            else -> {  /*do nothing*/
            }
        }
        super.onTrimMemory(level)
    }

    override fun localizedContext(): Context {
        return localeAwareContext(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAwareContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAwareContext(this)
    }

    private fun startTasks() {
        Thread(Runnable {
            com.starbase.bankwallet.core.App.Companion.rateAppManager.onAppLaunch()
            com.starbase.bankwallet.core.App.Companion.accountManager.loadAccounts()
            com.starbase.bankwallet.core.App.Companion.walletManager.loadWallets()
            com.starbase.bankwallet.core.App.Companion.adapterManager.preloadAdapters()
            com.starbase.bankwallet.core.App.Companion.accountManager.clearAccounts()
            com.starbase.bankwallet.core.App.Companion.notificationSubscriptionManager.processJobs()

            AppVersionManager(systemInfoManager,
                com.starbase.bankwallet.core.App.Companion.localStorage
            ).apply { storeAppVersion() }

        }).start()
    }
}
