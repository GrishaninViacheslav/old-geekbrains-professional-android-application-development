package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters

import androidx.annotation.CallSuper

abstract class MvpPresenter<T : View> {
    private var isFirstAttach = true

    private val attachedViews = arrayListOf<T>()

    protected fun attach(view: T) {
        if (isFirstAttach) {
            isFirstAttach = false
            onFirstViewAttach()
        }
        attachedViews.add(view)
    }

    protected fun forEachView(action: (view: T) -> Unit) {
        for (attachedView in attachedViews) {
            action(attachedView)
        }
    }

    open fun detach(view: T) {
        attachedViews.remove(view)
        if (attachedViews.isEmpty()) {
            onDestroy()
        }
    }

    protected open fun onFirstViewAttach() {}

    @CallSuper
    protected open fun onDestroy() {
        isFirstAttach = true
    }
}

