package br.com.igorbag.githubsearch.data

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("Name", Context.MODE_PRIVATE)

    fun storeName(key: String, name: String){
        preferences.edit().putString(key, name).apply()
    }

    fun getName(key: String) = preferences.getString(key, "")

}