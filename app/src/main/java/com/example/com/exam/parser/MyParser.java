package com.example.com.exam.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linke_000 on 01/11/2016.
 */

public class MyParser {
    public ArrayList<User> parseMagic(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();

        return gson.fromJson(json, listType);
    }
}
