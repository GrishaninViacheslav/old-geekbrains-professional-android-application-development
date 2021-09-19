package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.databinding.FragmentSearchInputBinding
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_input.SearchInputPresenter
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_input.SearchInputView
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.BackButtonListener

class SearchInputFragment : Fragment(), SearchInputView, BackButtonListener {
    private val view: FragmentSearchInputBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val presenter = SearchInputPresenter.getInstance(this)

    private fun setViewListeners() {
        with(view) {
            searchInput.setOnKeyListener { v, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    presenter.submitQuery(searchInput.text.toString())
                    return@setOnKeyListener true
                }
                view.errorMessage.text = ""
                return@setOnKeyListener false
            }
            searchConfirm.setOnClickListener {
                presenter.submitQuery(searchInput.text.toString())
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchInputFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = view.apply { setViewListeners() }.root

    override fun showMessage(message: String) {
        // TODO: Не могу разобраться с багом: если на экран SearchInputFragment вернуться из
        //             экрана SearchResultFragment (нажав в экране SearchResultFragment кнопку back), то
        //             почему то view(то что отображается на экране) перестаёт обновлятся при
        //             изменении свойств FragmentSearchInputBinding. Так например
        //             не отображается ошибка в view.errorMessage
        view.errorMessage.text = message
        Log.d("[SearchInputFragment]", "view.errorMessage.text: ${view.errorMessage.text}")
        Toast.makeText(context, "view.errorMessage.text: ${view.errorMessage.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach(this)
    }

    override fun backPressed() = presenter.backPressed()
}