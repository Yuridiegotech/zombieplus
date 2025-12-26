package support.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TvShowsApi {

  private final APIRequestContext request;
  private final String token;

  public TvShowsApi(Playwright playwright) {
    this.request = playwright.request().newContext(
        new APIRequest.NewContextOptions().setBaseURL("http://localhost:3333")
    );
    this.token = generateToken();
  }

  public void createTvShow(Map<String, Object> tvShow) {
    String title = (String) tvShow.get("title");
    String overview = (String) tvShow.get("overview");
    String company = (String) tvShow.get("company");
    String releaseYear = getIntegerValue(tvShow, "release_year");
    String season = getIntegerValue(tvShow, "season");
    String cover = (String) tvShow.get("cover");
    boolean featured = getBooleanValue(tvShow, "featured");

    System.out.println("========== CRIANDO TV SHOW VIA API ==========");
    System.out.println("Title: " + title);
    System.out.println("Overview: " + overview);
    System.out.println("Company: " + company);
    System.out.println("Release Year: " + releaseYear);
    System.out.println("Season: " + season);
    System.out.println("Cover: " + cover);
    System.out.println("Featured: " + featured);

    String companyId = getCompanies(company);

    System.out.println("\n--- Enviando requisição POST /tvshows ---");
    FormData formData = FormData.create()
        .set("title", title)
        .set("overview", overview)
        .set("company_id", companyId)
        .set("release_year", releaseYear)
        .set("seasons", season)
        .set("featured", String.valueOf(featured));

    if (cover != null && !cover.isEmpty()) {
      String coverPath = "src/test/java/support/fixtures/" + cover;
      System.out.println("Cover path: " + coverPath);
      formData.set("cover", Paths.get(coverPath));
    }

    APIResponse response = request.post("/tvshows",
        RequestOptions.create()
            .setHeader("Authorization", "Bearer " + token)
            .setMultipart(formData)
    );

    System.out.println("\n--- Resposta da API ---");
    System.out.println("Status Code: " + response.status());
    System.out.println("Status Text: " + response.statusText());
    System.out.println("Response Body: " + response.text());
    System.out.println("OK: " + response.ok());
    System.out.println("=============================================\n");
  }

  private String getIntegerValue(Map<String, Object> map, String key) {
    Object value = map.get(key);
    if (value instanceof Double) {
      return String.valueOf(((Double) value).intValue());
    }
    return String.valueOf(value);
  }

  private boolean getBooleanValue(Map<String, Object> map, String key) {
    Object value = map.get(key);
    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    return false;
  }

  private String generateToken() {
    String email = "admin@zombieplus.com";
    String password = "pwd123";

    System.out.println("\n========== GERANDO TOKEN (INICIALIZAÇÃO) ==========");
    System.out.println("POST /sessions");
    System.out.println("Email: " + email);
    System.out.println("===================================================");

    Map<String, String> data = new HashMap<>();
    data.put("email", email);
    data.put("password", password);

    APIResponse response = request.post("/sessions", RequestOptions.create()
        .setData(data));

    if (!response.ok()) {
      throw new RuntimeException(
          "Falha ao gerar token: " + response.status() + " - " + response.text());
    }

    Gson gson = new Gson();
    JsonObject responseBody = gson.fromJson(response.text(), JsonObject.class);
    String generatedToken = responseBody.get("token").getAsString();
    System.out.println("✓ Token gerado com sucesso!");
    System.out.println("===================================================\n");
    return generatedToken;
  }

  private String getCompanies(String companyName) {
    System.out.println("\n--- Buscando company_id ---");
    System.out.println("GET /companies?name=" + companyName);

    APIResponse response = request.get("/companies",
        RequestOptions.create()
            .setHeader("Authorization", "Bearer " + token)
            .setQueryParam("name", companyName)
    );

    if (!response.ok()) {
      throw new RuntimeException(
          "Falha ao buscar company: " + response.status() + " - " + response.text());
    }

    Gson gson = new Gson();
    JsonObject responseBody = gson.fromJson(response.text(), JsonObject.class);
    JsonArray data = responseBody.getAsJsonArray("data");

    if (data.size() == 0) {
      throw new RuntimeException("Company não encontrada: " + companyName);
    }

    String companyId = data.get(0).getAsJsonObject().get("id").getAsString();
    System.out.println("✓ Company ID encontrado: " + companyId);

    return companyId;
  }

  public void dispose() {
    if (request != null) {
      request.dispose();
    }
  }
}

