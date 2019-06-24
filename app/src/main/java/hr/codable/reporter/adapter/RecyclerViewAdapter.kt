package hr.codable.reporter.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hr.codable.reporter.R
import hr.codable.reporter.entity.Article
import java.text.SimpleDateFormat
import java.util.*


class RecyclerViewAdapter constructor(private val displayArticle: List<Article>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val articleItem = inflater.inflate(R.layout.article_item, parent, false)
        return ViewHolder(articleItem, context, displayArticle)
    }

    override fun getItemCount(): Int {
        return displayArticle.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(parent: ViewHolder, position: Int) {

        // to suppress warning
        val author: String? = displayArticle[position].author
        if (author.isNullOrBlank()) {
            parent.articleAuthorTextView.text = "Author unknown"
        } else {
            parent.articleAuthorTextView.text = displayArticle[position].author
        }

        val sourceDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val dateSource = sourceDateFormat.parse(displayArticle[position].publishedAt?.substring(0, 18))
        val goalDateFormat = SimpleDateFormat("MMMM dd, yyyy' at 'HH:mm", Locale.ENGLISH)
        val goalDate = goalDateFormat.format(dateSource)

        parent.articlePublishedAtTextView.text = goalDate.toString()
        parent.articleTitleTextView.text = displayArticle[position].title
        parent.articleSourceTextView.text = displayArticle[position].source?.name
    }

    class ViewHolder(itemView: View, context: Context, articles: List<Article>) : RecyclerView.ViewHolder(itemView) {

        val articleTitleTextView: TextView = itemView.findViewById(R.id.article_title_id)
        val articlePublishedAtTextView: TextView = itemView.findViewById(R.id.article_publishedAt_id)
        val articleAuthorTextView: TextView = itemView.findViewById(R.id.article_author_id)
        val articleSourceTextView: TextView = itemView.findViewById(R.id.article_source_id)

        init {

            itemView.setOnClickListener {

                val alertDialog = AlertDialog.Builder(context).create()
                alertDialog.setTitle(articles[adapterPosition].title)
                alertDialog.setMessage(articles[adapterPosition].description)
                alertDialog.show()
            }

            itemView.setOnLongClickListener {

                val url = Uri.parse(articles[adapterPosition].url)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = url
                startActivity(context, intent, null)
                true
            }
        }

    }
}