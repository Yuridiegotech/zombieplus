package support.fixtures.tvshows;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TvShowsData {

  private static Map<String, Map<String, Object>> tvShowsData;

  private static void loadTvShows() {
    if (tvShowsData == null) {
      try (BufferedReader reader = new BufferedReader(
          new FileReader("src/test/java/support/fixtures/tvshows/tvshows.json"))) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Object>>>() {
        }.getType();
        tvShowsData = gson.fromJson(reader, type);
      } catch (IOException e) {
        throw new RuntimeException("Erro ao carregar arquivo tvshows.json: " + e.getMessage(), e);
      }
    }
  }

  public static Map<String, Object> get(String tvShowKey) {
    loadTvShows();
    Map<String, Object> tvShow = tvShowsData.get(tvShowKey);
    if (tvShow == null) {
      throw new RuntimeException("TV Show não encontrado no JSON: " + tvShowKey);
    }
    return new HashMap<>(tvShow);
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

  public static boolean getBooleanValue(Map<String, Object> map, String key) {
    Object value = map.get(key);
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    throw new IllegalArgumentException("Valor não é booleano: " + value);
  }

  public static java.util.List<Map<String, Object>> getTvShowsList(Map<String, Object> map,
      String key) {
    Object value = map.get(key);
    if (value instanceof java.util.List) {
      return (java.util.List<Map<String, Object>>) value;
    }
    throw new IllegalArgumentException("Valor não é um array: " + value);
  }
}

