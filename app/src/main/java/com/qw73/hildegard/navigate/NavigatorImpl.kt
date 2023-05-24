package com.qw73.hildegard.navigate

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import com.qw73.hildegard.data.impl.resrc.IRes
import com.qw73.hildegard.navigate.Screen.AUTHORIZATION
import com.qw73.hildegard.navigate.Screen.MAIN
import com.qw73.hildegard.screens.authorization.AuthorizationActivity
import com.qw73.hildegard.screens.main.MainActivity
import javax.inject.Inject


class NavigatorImpl @Inject constructor(
    private val activity: FragmentActivity,
    val res: IRes,
) : Navigator {

    override fun navigateToAuthorization(screen: Screen, view: View, animationKey: String) {
        when (screen) {
            AUTHORIZATION -> {
                activity.startActivity(Intent(activity, AuthorizationActivity::class.java),
                    activityOptions(view, animationKey).toBundle())
            }
            MAIN -> {
                activity.startActivity(Intent(activity, MainActivity::class.java),
                    activityOptions(view, animationKey).toBundle())
            }
        }
    }

    override fun navigateToMain() {
        activity.startActivity(Intent(activity, MainActivity::class.java))

    }

    private fun activityOptions(view: View, animationKey: String): ActivityOptionsCompat {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            view,
            animationKey
        )
    }
}