package com.example.eatcleanapp.API;


import com.example.eatcleanapp.model.blogimages;
import com.example.eatcleanapp.model.blogs;
import com.example.eatcleanapp.model.comments;
import com.example.eatcleanapp.model.favoriterecipes;
import com.example.eatcleanapp.model.recipeimages;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.model.users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {
     public static final String URL = "https://msteatclean.000webhostapp.com/";
     Gson gson = new GsonBuilder()
             .setDateFormat("yyyy MM dd HH:mm:ss")
             .setLenient()
             .create();
     APIService apiService = new Retrofit.Builder()
             .baseUrl(URL)
             .addConverterFactory(GsonConverterFactory.create(gson))
             .build()
             .create(APIService.class);

     @GET("getUser.php")
     Call<List<users>> getUser();

     @GET("getAllUser.php")
     Call<List<users>> getAllUser();


     @GET("getRecipesNoImg.php")
     Call<List<recipes>> getRecipesNoImage();

     @POST("updateRecipe.php")
     @FormUrlEncoded
     Call<recipes> updateRecipeCtv(@Query("IDRecipes") String IDRecipes,
                                   @Field("RecipesTitle") String RecipesTitle,
                                   @Field("RecipesAuthor") String RecipesAuthor,
                                   @Field("RecipesContent") String RecipesContent,
                                   @Field("NutritionalIngredients") String NutritionalIngredients,
                                   @Field("Ingredients") String Ingredients,
                                   @Field("Steps") String Steps,
                                   @Field("Time") String Time,
                                   @Field("Status") String Status);
     @POST("updateRecipeImage.php")
     @FormUrlEncoded
     Call<recipeimages> updateRecipeImage (@Query("IDRecipesImages") String IDRecipesImages,
                                           @Field("RecipesImages") String RecipesImages);

     @POST("updateBlogImage.php")
     @FormUrlEncoded
     Call<blogimages> updateBlogImage (@Query("IDBlogImages") String IDBlogImages,
                                           @Field("BlogImages") String BlogImages);
     @GET("getBlogImages.php")
     Call<List<blogimages>> getImageBlog();

     @GET("getBlogs.php")
     Call<List<blogs>> getBlogs();

     @GET(" getBlogAll.php")
     Call<List<blogs>> getBlogsAll();

     @FormUrlEncoded
     @POST("updateBlog.php")
     Call<blogs> updateBlogCtv(@Query("IDBlog") String IDBlog,
                               @Field("BlogTitle") String BlogTitle,
                               @Field("BlogAuthor") String BlogAuthor,
                               @Field("BlogContent") String BlogContent,
                               @Field("Status") String Status);

     @POST("addBlog.php")
     @FormUrlEncoded
     Call<blogs> addBlogCtv(@Field("IDBlog") String IDBlog,
                            @Field("BlogTitle") String BlogTitle,
                            @Field("BlogAuthor") String BlogAuthor,
                            @Field("BlogContent") String BlogContent,
                            @Field("Time") String Time,
                            @Field("Status") String Status);

     @GET("getRecipes.php")
     Call<List<recipes>> getRecipes();


     @POST("addRecipes.php")
     @FormUrlEncoded
     Call<recipes> addRecipeCtv (@Field("IDRecipes") String IDRecipes,
                                 @Field("RecipesTitle") String RecipesTitle,
                                 @Field("RecipesAuthor") String RecipesAuthor,
                                 @Field("RecipesContent") String RecipesContent,
                                 @Field("NutritionalIngredients") String NutritionalIngredients,
                                 @Field("Ingredients") String Ingredients,
                                 @Field("Steps") String Steps,
                                 @Field("Time") String Time,
                                 @Field("Status") String Status,
                                 @Field("createTime") String createTime);
     @FormUrlEncoded
     @POST("addRecipeImages.php")
     Call<recipeimages> addRecipeImage (@Field("IDRecipes") String IDRecipes,
                                        @Field("IDRecipesImages") String IDRecipesImages,
                                        @Field("RecipesImages") String RecipesImages);
     @FormUrlEncoded
     @POST("addBlogImages.php")
     Call<blogimages> addBlogImage (@Field("IDBlogImages") String IDBlogImages,
                                        @Field("BlogImages") String BlogImages,
                                        @Field("IDBlog") String IDBlog);
     @FormUrlEncoded
     @POST("addFavoriteRecipes.php")
     Call<favoriterecipes> addFavoriteRecipes (@Field("IDUser") String IDUser,
                                               @Field("IDRecipes") String IDRecipes);

     @GET("getFavoriteRecipes_User.php")
     Call<List<recipes>> getFavorites_User(@Query("IDUser") String IDUser);

     @GET("deleteFavoriteRecipes.php")
     Call<favoriterecipes> deleteFavoriteRecipes(@Query("IDUser") String IDUser,
                                                 @Query("IDRecipes") String IDRecipes);
     @GET("getRecipeImages.php")
     Call<List<recipeimages>> getRecipeImages ();

     @POST("updateUser.php")
     @FormUrlEncoded
     Call<users> updateUser(@Query("IDUser") String IDUser,
                           @Field("Email") String Email,
                           @Field("FullName") String FullName);
     @GET("getUserByUsername.php")
     Call<users> getUserByUsername(@Query("Username") String Username);

     @POST("changePass.php")
     @FormUrlEncoded
     Call<users> changePass(@Query("IDUser") String IDUser,
                            @Field("Password") String Password);

     @Multipart
     @POST("uploadAvatar.php")
     Call<users> uploadImage(@Query("IDUser") String IDUser,
                             @Part MultipartBody.Part fileToUpload);

     @Multipart
     @POST("uploadImage.php")
     Call<String> uploadImage1 (@Part MultipartBody.Part fileToUpload);
     @FormUrlEncoded
     @POST("addComment.php")
     Call<comments> addComment(@Field("IDUser") String IDUser,
                               @Field("IDRecipes") String IDRecipes,
                               @Field("IDComment") String IDComment,
                               @Field("Comment") String Comment);

     @GET("deleteComment.php")
     Call<comments> deleteComment(@Query("IDComment") String IDComment);

     @GET("getCommentIDRecipes.php")
     Call<List<comments>> getCommentByRecipe(@Query("IDRecipes") String IDRecipes);

     @GET("getRecipeWaitingforApproval.php")
     Call<List<recipes>> getRecipeWaitingApproval();

     @GET("approveRecipe.php")
     Call<recipes> approveRecipe(@Query("IDRecipes") String IDRecipes);

     @GET("denyRecipe.php")
     Call<recipes> denyRecipe(@Query("IDRecipes") String IDRecipes);

     @GET("getBlogWaitingforApproval.php")
     Call<List<blogs>> getBlogWaitingApproval();

     @GET("approvalBlog.php")
     Call<blogs> approveBlog(@Query("IDBlog") String IDBlog);

     @GET("denyBlog.php")
     Call<blogs> denyBlog(@Query("IDBlog") String IDBlog);

     @GET("approvalUser.php")
     Call<users> approvalUser(@Query("IDUser") String IDUser);

     @GET("denyUser.php")
     Call<users> denyUser(@Query("IDUser") String IDUser);

     @GET("lockUser.php")
     Call<users> lockUser(@Query("IDUser") String IDUser);

     @GET("unlockUser.php")
     Call<users> unlockUser(@Query("IDUser") String IDUser);

     @GET("getUserMonth.php")
     Call<JsonArray> getUserMonth(@Query("Year") String Year);

     @GET("getBlogMonth.php")
     Call<JsonArray> getBlogMonth(@Query("Year") String Year);

     @GET("getRecipeMonth.php")
     Call<JsonArray> getRecipeMonth(@Query("Year") String Year);
}
