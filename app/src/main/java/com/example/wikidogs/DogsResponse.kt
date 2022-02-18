package com.example.wikidogs

import com.google.gson.annotations.SerializedName

data class DogsResponse (@SerializedName("message") var images: List<String>,
                         @SerializedName("status") var status: String)

/*Creo el data class utilizando @SerializedName para especificar los nombres exactos
de las variables que devuelve la API. De esta manera, puedo poner el nombre que yo quiera a las
variables que creé.*/