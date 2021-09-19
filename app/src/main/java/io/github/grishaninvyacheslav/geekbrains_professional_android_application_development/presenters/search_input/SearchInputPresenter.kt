package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_input

import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.R
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.MvpPresenter
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.App
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.Screens

class SearchInputPresenter(private val router: Router = App.instance.router) :
    MvpPresenter<SearchInputView>() {
    private fun validate(query: String) =
        (!query.contains(" ") && query.isNotBlank()).also { isValid ->
            if (!isValid) {
                if (query.contains(" ")) {
                    forEachView { it.showMessage(App.instance.getString(R.string.query_is_invalid_more_than_one_word)) }
                } else if (query.isBlank()) {
                    forEachView { it.showMessage(App.instance.getString(R.string.query_is_invalid_more_than_no_word)) }
                }
            }
        }

    companion object {
        @Volatile
        private var INSTANCE: SearchInputPresenter? = null

        fun getInstance(view: SearchInputView): SearchInputPresenter =
            (INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchInputPresenter().also { INSTANCE = it }
            }).apply { attach(view) }
    }

    fun submitQuery(query: String) {
        if (validate(query)) {
            router.navigateTo(Screens.searchResult(query))
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}