package com.example.wikidogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikidogs.databinding.ActivityMainBinding
import com.example.wikidogs.recyclerview.DogAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    //SearchView.OnQueryTextListener
    // para agregar un listener al view de SearchView que tengo en el layout de ActivityMain
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private var dogImages: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        binding.svBuscar.setOnQueryTextListener(this)
    }

    private fun initRecyclerView(){
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByBreed(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            var call = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")

            var puppies = call.body()
            runOnUiThread {
                if(call.isSuccessful){
                    val images = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@MainActivity, "No tengo imagenes de esta raza", Toast.LENGTH_LONG).show()
                }
                hideKeyboard()
            }

        }
    }
    
    private fun hideKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    //Estas funciones se implementan luego de agregar el listener de SearchView
    override fun onQueryTextSubmit(query: String): Boolean {
        if(!query.isNullOrEmpty()){
            searchByBreed(query!!.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}