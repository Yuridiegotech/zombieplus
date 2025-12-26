package support.api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.util.HashMap;
import java.util.Map;

public class LeadsApi {

  private APIRequestContext request;

  public LeadsApi(Playwright playwright) {
    this.request = playwright.request().newContext(
        new APIRequest.NewContextOptions().setBaseURL("http://localhost:3333")
    );
  }

  public APIResponse createLead(String name, String email) {
    System.out.println("========== CRIANDO LEAD VIA API ==========");
    System.out.println("Name: " + name);
    System.out.println("Email: " + email);

    Map<String, String> data = new HashMap<>();
    data.put("name", name);
    data.put("email", email);

    System.out.println("\n--- Enviando requisição POST /leads ---");
    APIResponse response = request.post("/leads",
        RequestOptions.create().setData(data)
    );

    System.out.println("\n--- Resposta da API ---");
    System.out.println("Status Code: " + response.status());
    System.out.println("Status Text: " + response.statusText());
    System.out.println("Response Body: " + response.text());
    System.out.println("OK: " + response.ok());
    System.out.println("==========================================\n");

    return response;
  }

  public void dispose() {
    if (request != null) {
      request.dispose();
    }
  }
}

