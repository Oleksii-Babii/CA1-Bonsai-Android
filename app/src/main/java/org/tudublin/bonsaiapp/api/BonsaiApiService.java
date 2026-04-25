package org.tudublin.bonsaiapp.api;

import org.tudublin.bonsaiapp.model.Species;
import org.tudublin.bonsaiapp.model.Tree;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BonsaiApiService {

    @GET("/api/species")
    Call<List<Species>> getAllSpecies();

    @GET("/api/trees")
    Call<List<Tree>> getAllTrees();
}