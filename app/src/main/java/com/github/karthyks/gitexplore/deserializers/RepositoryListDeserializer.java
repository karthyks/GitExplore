package com.github.karthyks.gitexplore.deserializers;

import com.github.karthyks.gitexplore.model.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class RepositoryListDeserializer implements JsonDeserializer<List<Repository>> {
    public static final String TAG = RepositoryListDeserializer.class.getSimpleName();

    @Override
    public List<Repository> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = new Gson();
        List<Repository> repositories = new LinkedList<>();
        JsonElement itemsElement;
        if (json.isJsonArray()) {
            itemsElement = json;
        } else {
            itemsElement = json.getAsJsonObject().get("items");
            if (itemsElement == null) return repositories;
        }
        for (JsonElement item : itemsElement.getAsJsonArray()) {
            try {
                Repository repository = gson.fromJson(item, Repository.class);
                repositories.add(repository);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return repositories;
    }
}
