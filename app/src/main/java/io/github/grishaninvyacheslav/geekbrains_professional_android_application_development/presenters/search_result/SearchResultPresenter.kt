package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_result

import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.R
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.domain.models.DictionaryWordDto
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.domain.models.repository.DictionaryRepository
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.domain.models.repository.IDictionaryRepository
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.domain.schedulers.DefaultSchedulers
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.MvpPresenter
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.ApiHolder
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.App
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

class SearchResultPresenter(
    private val query: String,
    private var disposables: CompositeDisposable = CompositeDisposable(),
    private val repository: IDictionaryRepository = DictionaryRepository(ApiHolder.api),
    private val router: Router = App.instance.router
) :
    MvpPresenter<SearchResultView>() {
    private inner class DefinitionsLoadObserver :
        DisposableSingleObserver<List<DictionaryWordDto>>() {
        override fun onSuccess(value: List<DictionaryWordDto>) {
            forEachView { it.setTitle(value[0].word, value[0].phonetic) }
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
            when (error) {
                is java.net.UnknownHostException ->
                    forEachView { it.showErrorMessage(App.instance.getString(R.string.error_500)) }
                is retrofit2.adapter.rxjava2.HttpException ->
                    forEachView { it.showErrorMessage(App.instance.getString(R.string.error_404)) }
            }
        }
    }

    private fun loadDefinitions() {
        disposables.add(
            repository
                .getDefinitions(query)
                .observeOn(DefaultSchedulers.main())
                .subscribeWith(DefinitionsLoadObserver())
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchResultPresenter? = null

        fun getInstance(
            view: SearchResultView,
            query: String
        ): SearchResultPresenter =
            (INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchResultPresenter(query).also { INSTANCE = it }
            }).apply { attach(view) }
    }

    override fun onFirstViewAttach() {
        loadDefinitions()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        INSTANCE = null
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}