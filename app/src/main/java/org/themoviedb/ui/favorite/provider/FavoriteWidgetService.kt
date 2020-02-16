package org.themoviedb.ui.favorite.provider

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import org.themoviedb.R
import org.themoviedb.data.local.models.Movie
import org.themoviedb.data.local.room.FavoriteDatabase

class FavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        FavoriteWidgetFactory(this.applicationContext)
}

class FavoriteWidgetFactory constructor(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private val movieDao = FavoriteDatabase.getInstance(context).movieDao()

    private var widgetItems = emptyList<Movie>()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        widgetItems = movieDao.selectAllForWidget()
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val widgetItem = widgetItems[position]
        val views = RemoteViews(context.packageName, R.layout.widget_favorite_item).apply {
            setTextViewText(R.id.widget_title, widgetItem.title)
            setTextViewText(R.id.widget_date, widgetItem.date)
        }

        try {
            val bitmap = Picasso.get()
                .load("https://image.tmdb.org/t/p/original/${widgetItem.backgroundImage}")
                .resizeDimen(R.dimen.widget_width, R.dimen.widget_height)
                .get()
            views.setImageViewBitmap(R.id.widget_image, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return views
    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        widgetItems = emptyList()
        compositeDisposable.clear()
    }
}