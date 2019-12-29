package org.themoviedb.screens.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import org.themoviedb.databinding.FragmentDetailBinding
import org.themoviedb.data.models.Movie
import org.themoviedb.screens.main.viewmodel.DetailViewModel
import org.themoviedb.adapter.DetailListCastAdapter
import org.themoviedb.utils.CustomDialog
import org.themoviedb.utils.ext.observe
import javax.inject.Inject

class DetailFragment : DaggerFragment() {

    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var customDialog: CustomDialog

    private val viewModel: DetailViewModel by viewModels { viewModelFactory }

    private val movieArgs: DetailFragmentArgs by navArgs()

    private val movie: Movie by lazy { movieArgs.movie }

    private val adapter by lazy { DetailListCastAdapter() }

    private val activity: MainActivity by lazy { getActivity() as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DetailFragment.viewModel.apply { setMovieDetail(this@DetailFragment.movie) }
            backButton.setOnClickListener { findNavController().popBackStack() }
            recyclerViewCast.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = this@DetailFragment.adapter
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        activity.supportActionBar?.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        observe(viewModel.getLoading()) { isLoading ->
            if (isLoading) {
                binding.apply {
                    castView.visibility = View.GONE
                    shimmerView.visibility = View.VISIBLE
                    shimmerView.startShimmer()
                }
            } else {
                binding.apply {
                    shimmerView.stopShimmer()
                    shimmerView.visibility = View.GONE
                    castView.visibility = View.VISIBLE
                }
            }
        }

        observe(viewModel.getMovieCasts()) { casts -> adapter.loadItems(casts) }
        observe(viewModel.onFavoriteAdded) { customDialog.showAddToFavoriteDialog(requireContext()) }
        observe(viewModel.onFavoriteRemoved) { customDialog.showRemoveFromFavoriteDialog(requireContext()) }
    }
}