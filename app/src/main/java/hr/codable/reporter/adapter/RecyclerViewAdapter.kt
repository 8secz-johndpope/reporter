package hr.codable.reporter.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import hr.codable.reporter.R
import hr.codable.reporter.entity.Article

class RecyclerViewAdapter constructor(private val displayArticle: List<Article>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val articleItem = inflater.inflate(R.layout.article_item, parent, false)
        return ViewHolder(articleItem, context)
    }

    override fun getItemCount(): Int {
        return displayArticle.size
    }

    override fun onBindViewHolder(parent: ViewHolder, position: Int) {

        parent.articleAuthorTextView.text = displayArticle.get(position).author
        parent.articlePublishedAtTextView.text = displayArticle.get(position).publishedAt
        parent.articleTitleTextView.text = displayArticle.get(position).title
        parent.articleSourceTextView.text = displayArticle.get(position).source.toString()

    }

    class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        val articleTitleTextView: TextView = itemView.findViewById(R.id.article_title_id)
        val articlePublishedAtTextView: TextView = itemView.findViewById(R.id.article_publishedAt_id)
        val articleAuthorTextView: TextView = itemView.findViewById(R.id.article_author_id)
        val articleSourceTextView: TextView = itemView.findViewById(R.id.article_source_id)

        init {

            itemView.setOnClickListener {

                Toast.makeText(context, "Clicked on ${articleTitleTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}