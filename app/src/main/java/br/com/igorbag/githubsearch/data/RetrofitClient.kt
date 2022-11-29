package br.com.igorbag.githubsearch.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun createGitHubService() : GitHubService{
            return getRetrofitInstance().create(GitHubService::class.java)
        }
    }

}


/*

         Documentacao oficial do retrofit - https://square.github.io/retrofit/
         URL_BASE da API do  GitHub= https://api.github.com/
         lembre-se de utilizar o GsonConverterFactory mostrado no curso
      */