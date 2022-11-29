package br.com.igorbag.githubsearch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.SecurityPreferences
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.data.RetrofitClient
import br.com.igorbag.githubsearch.databinding.ActivityMainBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var githubApi: GitHubService
    private lateinit var binding: ActivityMainBinding
    private lateinit var nameUser: String
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //layout
        binding.rvListaRepositories.layoutManager = LinearLayoutManager(this)


        setupUser()
        setupListeners()
        showUserName()
        setupRetrofit()
    }

    override fun onResume() {
        super.onResume()
        getAllReposByUserName()
    }

    private fun setupUser() {
        nameUser = binding.etNomeUsuario.text.toString()
    }

    //metodo responsavel por configurar os listeners click da tela
    private fun setupListeners() {
        binding.btnConfirmar.setOnClickListener {
            saveUserLocal()
            onResume()
        }

    }

    // salvar o usuario preenchido no EditText utilizando uma SharedPreferences
    private fun saveUserLocal() {
        setupUser()
        SecurityPreferences(this).storeName("USER_NAME", nameUser)
    }

    private fun showUserName() {
        binding.etNomeUsuario.setText(SecurityPreferences(this).getName("USER_NAME"))
    }

    //Metodo responsavel por fazer a configuracao base do Retrofit
    private fun setupRetrofit() {
        githubApi = RetrofitClient.createGitHubService()
    }

    //Metodo responsavel por buscar todos os repositorios do usuario fornecido
    private fun getAllReposByUserName() {
        if (nameUser.isEmpty()) {
            setupUser()

        } else {
            githubApi.getAllRepositoriesByUser(nameUser)
                .enqueue(object : Callback<List<Repository>> {
                    override fun onResponse(
                        call: Call<List<Repository>>, response: Response<List<Repository>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                setupAdapter(it)
                            }
                        } else {
                            Toast.makeText(applicationContext, "error Callback", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, R.string.response_error, Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    // Metodo responsavel por realizar a configuracao do adapter
    fun setupAdapter(list: List<Repository>) {
        adapter = RepositoryAdapter(
            list
        ) {
            shareRepositoryLink(it)
        }
        binding.rvListaRepositories.adapter = adapter
    }

    // Metodo responsavel por compartilhar o link do repositorio selecionado
    // @Todo 11 - Colocar esse metodo no click do share item do adapter
    private fun shareRepositoryLink(urlRepository: String) {
        Log.d("URL", urlRepository)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // Metodo responsavel por abrir o browser com o link informado do repositorio
    // @Todo 12 - Colocar esse metodo no click item do adapter
    fun openBrowser(htmlUrl: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse(htmlUrl)
            )
        )
    }
}