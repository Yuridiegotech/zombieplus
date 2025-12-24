package support.fixtures;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MoviesData {

    private static Map<String, Map<String, Object>> moviesData;

    private static void loadMovies() {
        if (moviesData == null) {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/test/java/support/fixtures/movies.json"))) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
                moviesData = gson.fromJson(reader, type);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao carregar arquivo movies.json: " + e.getMessage(), e);
            }
        }
    }

    public static Map<String, Object> get(String movieKey) {
        loadMovies();
        Map<String, Object> movie = moviesData.get(movieKey);
        if (movie == null) {
            throw new RuntimeException("Filme não encontrado no JSON: " + movieKey);
        }
        // Retorna uma cópia para evitar modificações acidentais
        return new HashMap<>(movie);
    }

    public static String getStringValue(Map<String, Object> map, String key) {
        return (String) map.get(key);
    }

    public static String getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Double) {
            return String.valueOf(((Double) value).intValue());
        }
        return String.valueOf(value);
    }
}

