package io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.databinding.FragmentSearchResultBinding
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_result.SearchResultPresenter
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.presenters.search_result.SearchResultView
import io.github.grishaninvyacheslav.geekbrains_professional_android_application_development.views.BackButtonListener

class SearchResultFragment : Fragment(), SearchResultView, BackButtonListener {
    private val view: FragmentSearchResultBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private lateinit var presenter: SearchResultPresenter

    companion object {
        val QUERY_ARG = "QUERY"

        @JvmStatic
        fun newInstance(query: String) = SearchResultFragment().apply {
            arguments = Bundle().apply { putString(QUERY_ARG, query) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SearchResultPresenter.getInstance(
            this,
            requireArguments().getString(QUERY_ARG)!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = view.root

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach(this)
    }

    override fun setTitle(wordValue: String, phoneticValue: String) = with(view) {
        word.text = wordValue
        phonetic.text = phoneticValue
        progressBar.visibility = GONE
        errorMessage.visibility = GONE
    }

    override fun showErrorMessage(message: String) = with(view.errorMessage) {
        text = message
        visibility = VISIBLE
    }

    override fun backPressed() = presenter.backPressed()
}