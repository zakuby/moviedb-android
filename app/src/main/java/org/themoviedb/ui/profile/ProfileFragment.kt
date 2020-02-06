package org.themoviedb.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.themoviedb.R
import org.themoviedb.databinding.FragmentProfileBinding
import org.themoviedb.ui.base.BaseFragment
import org.themoviedb.ui.main.WebViewActivity
import org.themoviedb.utils.ext.observe
import org.themoviedb.utils.ext.toast
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding.viewModel = this@ProfileFragment.viewModel
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun subscribeUI() {
        observe(viewModel.getProfile()) { profile ->
            binding.apply {
                profileGithub.setOnClickListener { openWebView(profile.githubUrl, "Github Profile") }
                profileDicoding.setOnClickListener { openWebView(profile.dicodingProfileUrl, "Dicoding Profile") }
                profileEmail.setOnClickListener { goToEmailIntent(profile.email) }
                profileWhatsapp.setOnClickListener { goToWhatsAppIntent(profile.phone) }
            }
        }
    }

    private fun goToEmailIntent(email: String) {

        val subject = "The Movie DB Android Application"
        val message = "Hi Muhammad Yaqub," + "\n" + "Nice Android Application !"

        val i = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            type = "message/rfc822"
        }

        startActivity(Intent.createChooser(i, "Send email :"))
    }

    private fun openWebView(url: String, title: String) {
        val i = Intent(activity, WebViewActivity::class.java).apply {
            putExtra("url", url)
            putExtra("title", title)
        }

        startActivity(i)
    }

    private fun goToWhatsAppIntent(phone: String) {
        val whatsAppPackageName = "com.whatsapp"
        try {
            val message = "Hi Muhammad Yaqub," + "\n" + "Nice Android Application !"
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra("jid", "$phone@s.whatsapp.net")
                setPackage(whatsAppPackageName)
            }
            startActivity(i)
        } catch (e: Exception) {
            requireActivity().toast(R.string.error_no_whatsapp)
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$whatsAppPackageName")
                    )
                )
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$whatsAppPackageName")
                    )
                )
            }
        }
    }
}