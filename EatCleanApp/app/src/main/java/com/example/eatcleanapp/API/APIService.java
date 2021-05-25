package com.example.eatcleanapp.API;


import com.example.eatcleanapp.model.users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
     Gson gson = new GsonBuilder()
             .setDateFormat("yyyy-MM-dd HH:mm:ss")
             .create();
     APIService apiService = new Retrofit.Builder()
             .baseUrl("https://msteatclean.000webhostapp.com/")
             .addConverterFactory(GsonConverterFactory.create(gson))
             .build()
             .create(APIService.class);

     @GET("getUser.php")
     Call<List<users>> getUser();
     @POST("addFavoriteRecipes.php")
     @FormUrlEncoded
     Call<POST> addFavoriteRecipes (@Field("IDUser") String IDUser,
                                    @Field("IDRecipes") String IDRecipes);
}
