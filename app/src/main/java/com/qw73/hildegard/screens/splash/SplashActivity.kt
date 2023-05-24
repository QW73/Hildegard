package com.qw73.hildegard.screens.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.qw73.hildegard.R
import com.qw73.hildegard.base.BaseActivity
import com.qw73.hildegard.data.DELAY_HOME
import com.qw73.hildegard.data.DELAY_LOGIN
import com.qw73.hildegard.data.KEY_USER_AUTHORIZATION
import com.qw73.hildegard.data.impl.prefs.IPref
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.databinding.ActivitySplashBinding
import com.qw73.hildegard.navigate.Navigator
import com.qw73.hildegard.navigate.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    lateinit var logoImage: ImageView
    lateinit var developerView: TextView

    @Inject lateinit var navigor: Navigator
    @Inject lateinit var pref: IPref
    @Inject lateinit var res: IRes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logoImage = (viewBinding as ActivitySplashBinding).imageLogo
        logoImage.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        developerView = (viewBinding as ActivitySplashBinding).viewDevelopedBy
        developerView.text = res.str(R.string.developer)
    }

    override fun onResume() {
        super.onResume()
        logoImage.animate().setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                loadNextScreen(if (pref.bool(KEY_USER_AUTHORIZATION)) Screen.MAIN else Screen.AUTHORIZATION,
                    if (pref.bool(KEY_USER_AUTHORIZATION)) DELAY_HOME else DELAY_LOGIN)
            }
        }).start()
    }

    private fun loadNextScreen(screen: Screen, delay: Long) {
        /* additional delay to show the splash */
        Handler(Looper.getMainLooper()).postDelayed({
            navigor.navigateToAuthorization(screen, logoImage, res.str(R.string.image_logo_transition))
        }, delay)
    }

    override fun binding(): ViewBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}