package paypay.currencycoversion.roots

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.multidex.MultiDexApplication
import paypay.currencycoversion.BuildConfig
import timber.log.Timber

class CurrencyConversionApplication : MultiDexApplication(), LifecycleEventObserver {

    override fun onCreate() {
        super.onCreate()
        configureLogging()
    }

    private fun configureLogging() {
        val debugTree: Timber.Tree = object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                var logPrefix: String
                logPrefix = String.format("(%s:%s)#%s",
                        element.fileName,
                        element.lineNumber,
                        element.methodName)
                logPrefix = "$logPrefix [ RidmikLog:  ] "
                return logPrefix
            }
        }
        val LOG_MSG: String
        if (BuildConfig.DEBUG) {
            Timber.plant(debugTree)
            LOG_MSG = "Logging configured for debug build"
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

                }
            })
            LOG_MSG = "Logging configured for release build"
        }
        Timber.d(LOG_MSG)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    }
}