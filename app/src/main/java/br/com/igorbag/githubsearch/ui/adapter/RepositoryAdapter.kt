package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.databinding.RepositoryItemBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.MainActivity

class RepositoryAdapter(
    private val repositories: List<Repository>,
    private val click : (url: String) -> Unit
    ) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RepositoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // Pega o conteudo da view e troca pela informacao de item de uma lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.binding.apply {
            tvNameRepos.text = repository.name
            ivLink.setOnClickListener {
                click(repository.htmlUrl)
                //MainActivity().openBrowser(repository.htmlUrl)
            }
        }
    }

    // Pega a quantidade de repositorios da lista
    override fun getItemCount(): Int = repositories.size

    class ViewHolder(val binding: RepositoryItemBinding) : RecyclerView.ViewHolder(binding.root)


}


