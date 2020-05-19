package ua.nulp.rozrhakha.utils;


import com.google.gson.Gson;
import ua.nulp.rozrhakha.ga.Generation;

import java.io.*;

public class JsonUtils<T> {

    private Gson gson;

    public JsonUtils() {
        this.gson = new Gson();
    }

    public Generation deserialize(String path) throws  FileNotFoundException{
        Reader reader = new FileReader(path);
        return gson.fromJson(reader, Generation.class);
    }

    public void serialize(String path, T data) throws IOException {
        Writer writer = new FileWriter(path);
        gson.toJson(data, writer);
        writer.flush();
        writer.close();
    }
}
