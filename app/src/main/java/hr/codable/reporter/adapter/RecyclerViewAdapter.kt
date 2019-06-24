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
import android.widget.Toast
import hr.codable.reporter.R
import hr.codable.reporter.entity.Article
import java.text.ParseException
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

        val title: String? = displayArticle[position].title
        if (title.isNullOrBlank()) {
            parent.articleTitleTextView.text = "Title not found"
        } else {
            parent.articleTitleTextView.text = title
        }

        val author: String? = displayArticle[position].author
        if (author.isNullOrBlank()) {
            parent.articleAuthorTextView.text = "Author not found"
        } else {
            parent.articleAuthorTextView.text = author
        }

        val source: String? = displayArticle[position].source?.name
        if (source.isNullOrBlank()) {
            parent.articleSourceTextView.text = "Source not found"
        } else {
            parent.articleSourceTextView.text = source

        }

        try {
            val sourceDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
            val dateSource = sourceDateFormat.parse(displayArticle[position].publishedAt?.substring(0, 15))
            val goalDateFormat = SimpleDateFormat("MMMM dd, yyyy' at 'HH:mm", Locale.ENGLISH)
            val goalDate = goalDateFormat.format(dateSource)
            parent.articlePublishedAtTextView.text = goalDate.toString()
        } catch (exc: ParseException) {
            parent.articlePublishedAtTextView.text = "Unparseable date"
        }
    }

    class ViewHolder(itemView: View, context: Context, articles: List<Article>) : RecyclerView.ViewHolder(itemView) {

        val articleTitleTextView: TextView = itemView.findViewById(R.id.article_title_id)
        val articlePublishedAtTextView: TextView = itemView.findViewById(R.id.article_publishedAt_id)
        val articleAuthorTextView: TextView = itemView.findViewById(R.id.article_author_id)
        val articleSourceTextView: TextView = itemView.findViewById(R.id.article_source_id)

        init {

            itemView.setOnClickListener {

                val alertDialog = AlertDialog.Builder(context).create()

                val title: String? = articles[adapterPosition].title
                if (title.isNullOrBlank()) {
                    alertDialog.setTitle("Title not found")
                } else {
                    alertDialog.setTitle(title)
                }

                val description = articles[adapterPosition].description
                if (description.isNullOrBlank()) {
                    alertDialog.setMessage("Description not found")
                } else {
                    alertDialog.setMessage(description)
                }

                alertDialog.show()
            }

            itemView.setOnLongClickListener {

                val uri = articles[adapterPosition].url
                if (uri.isNullOrBlank()) {
                    Toast.makeText(context, "No url available", Toast.LENGTH_SHORT).show()
                    false
                } else {
                    val url = Uri.parse(articles[adapterPosition].url)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = url
                    startActivity(context, intent, null)
                    true
                }
            }
        }

    }
}