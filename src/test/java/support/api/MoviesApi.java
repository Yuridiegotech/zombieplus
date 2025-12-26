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

public class MoviesApi {

  private final APIRequestContext request;
  private final String token;

  public MoviesApi(Playwright playwright) {
    this.request = playwright.request().newContext(
        new APIRequest.NewContextOptions().setBaseURL("http://localhost:3333")
    );
    // Gera o token uma única vez ao inicializar
    this.token = generateToken();
  }

  public void createMovie(Map<String, Object> movie) {
    // Extrai os dados do movie
    String title = (String) movie.get("title");
    String overview = (String) movie.get("overview");
    String company = (String) movie.get("company");
    String releaseYear = getIntegerValue(movie, "release_year");
    String cover = (String) movie.get("cover");
    boolean featured = getBooleanValue(movie, "featured");

    System.out.println("========== CRIANDO FILME VIA API ==========");
    System.out.println("Title: " + title);
    System.out.println("Overview: " + overview);
    System.out.println("Company: " + company);
    System.out.println("Release Year: " + releaseYear);
    System.out.println("Cover: " + cover);
    System.out.println("Featured: " + featured);

    // 1. Busca o company_id
    String companyId = getCompanies(company);

    // 2. Cria o filme via API
    System.out.println("\n--- Enviando requisição POST /movies ---");
    FormData formData = FormData.create()
        .set("title", title)
        .set("overview", overview)
        .set("company_id", companyId)
        .set("release_year", releaseYear)
        .set("featured", String.valueOf(featured));

    if (cover != null && !cover.isEmpty()) {
      String coverPath = "src/test/java/support/fixtures/" + cover;
      System.out.println("Cover path: " + coverPath);
      formData.set("cover", Paths.get(coverPath));
    }

    APIResponse response = request.post("/movies",
        RequestOptions.create()
            .setHeader("Authorization", "Bearer " + token)
            .setMultipart(formData)
    );

    System.out.println("\n--- Resposta da API ---");
    System.out.println("Status Code: " + response.status());
    System.out.println("Status Text: " + response.statusText());
    System.out.println("Response Body: " + response.text());
    System.out.println("OK: " + response.ok());
    System.out.println("==========================================\n");
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
    System.out.println(
        "Request Body: {\"email\":\"" + email + "\", \"password\":\"" + password + "\"}");

    Map<String, String> data = new HashMap<>();
    data.put("email", email);
    data.put("password", password);

    Gson gson = new Gson();
    APIResponse response = request.post("/sessions",
        RequestOptions.create()
            .setHeader("Content-Type", "application/json")
            .setData(gson.toJson(data))
    );

    System.out.println("Response Status: " + response.status());
    System.out.println("Response Body: " + response.text());

    JsonObject responseBody = gson.fromJson(response.text(), JsonObject.class);
    String generatedToken = responseBody.get("token").getAsString();
    System.out.println("✓ Token gerado com sucesso!");
    System.out.println("===================================================\n");
    return generatedToken;
  }

  private String getCompanies(String companyName) {
    System.out.println("\n--- Buscando Company ID ---");
    System.out.println("GET /companies?name=" + companyName);

    APIResponse response = request.get("/companies",
        RequestOptions.create()
            .setHeader("Authorization", "Bearer " + token)
            .setQueryParam("name", companyName)
    );

    System.out.println("Response Status: " + response.status());
    System.out.println("Response Body: " + response.text());

    Gson gson = new Gson();
    JsonObject responseBody = gson.fromJson(response.text(), JsonObject.class);

    // Pega o array 'data' e extrai o primeiro item [0].id
    JsonArray data = responseBody.getAsJsonArray("data");

    if (data != null && !data.isEmpty()) {
      JsonObject firstCompany = data.get(0).getAsJsonObject();
      String companyId = firstCompany.get("id").getAsString();
      System.out.println("Company ID encontrado: " + companyId);
      return companyId;
    }

    throw new RuntimeException("Company não encontrada: " + companyName);
  }


  public void dispose() {
    if (request != null) {
      request.dispose();
    }
  }
}

