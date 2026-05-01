package org.tudublin.bonsaiapp.api;

import org.tudublin.bonsaiapp.model.Species;
import org.tudublin.bonsaiapp.model.Tree;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BonsaiApiService {

    @GET("/api/species")
    Call<List<Species>> getAllSpecies();

    @GET("/api/species/{id}")
    Call<Species> getSpecies(@Path("id") int id);

    @GET("/api/trees")
    Call<List<Tree>> getAllTrees();

    @GET("/api/trees/{id}")
    Call<Tree> getTree(@Path("id") int id);
}