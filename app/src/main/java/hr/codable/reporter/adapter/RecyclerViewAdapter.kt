package hr.codable.reporter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import hr.codable.reporter.R
import hr.codable.reporter.entity.Article
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(parent: ViewHolder, position: Int) {

        // to suppress warning
        val author: String? = displayArticle[position].author
        if (author.isNullOrBlank()) {
            parent.articleAuthorTextView.text = "Author unknown"
        } else {
            parent.articleAuthorTextView.text = displayArticle.get(position).author
        }
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        parent.articlePublishedAtTextView.text = LocalDate.parse(
            displayArticle[position]
                .publishedAt.substring(0, 10)
        ).format(formatter).toString()
        parent.articleTitleTextView.text = displayArticle[position].title
        parent.articleSourceTextView.text = displayArticle[position].source.name
    }

    class ViewHolder(itemView: View, context: Context, articles: List<Article>) : RecyclerView.ViewHolder(itemView) {

        val articleTitleTextView: TextView = itemView.findViewById(R.id.article_title_id)
        val articlePublishedAtTextView: TextView = itemView.findViewById(R.id.article_publishedAt_id)
        val articleAuthorTextView: TextView = itemView.findViewById(R.id.article_author_id)
        val articleSourceTextView: TextView = itemView.findViewById(R.id.article_source_id)

        init {

            itemView.setOnClickListener {

                Toast.makeText(context, "Clicked on ${articleTitleTextView.text}", Toast.LENGTH_SHORT).show()
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