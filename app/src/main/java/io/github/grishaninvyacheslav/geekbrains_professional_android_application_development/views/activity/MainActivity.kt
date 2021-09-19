package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.R
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.databinding.ActivityMainBinding
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.main.MainPresenter
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.main.MainView
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.App
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.BackButtonListener

class MainActivity : AppCompatActivity(), MainView, BackButtonListener {
    private val view: ActivityMainBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val presenter = MainPresenter.getInstance(this)
    private val navigator = AppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.instance.navigatorHolder.removeNavigator()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach(this)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BackButtonListener && it.backPressed()) {
                return
            }
        }
    }

    override fun backPressed() = presenter.backPressed()
}