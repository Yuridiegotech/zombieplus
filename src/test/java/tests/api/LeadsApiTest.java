package tests.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LeadsApiTest {
  private static final String BASE_URL = "http://localhost:3333";

  private HttpResponse<String> postLead(String name, String email) throws IOException, InterruptedException {
    String jsonBody = String.format("{\"name\":\"%s\",\"email\":\"%s\"}", name, email);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/leads"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    // Em qualquer falha, a mensagem já traz o payload e o response para facilitar análise
    assertNotNull(response, "Response nulo para payload: " + jsonBody);
    assertNotNull(response.body(),
        "Response body nulo. Payload enviado: " + jsonBody + ", statusCode: " + response.statusCode());

    return response;
  }

  @Test
  @DisplayName("Deve criar lead com payload válido (novo lead)")
  void shouldCreateNewLeadWithValidPayload() throws IOException, InterruptedException {
    String uniqueEmail = "teste+" + UUID.randomUUID() + "@teste1.com";
    String name = "Diego Yuri Ribeiro Pereira Lima";

    HttpResponse<String> response = postLead(name, uniqueEmail);
    String body = response.body();

    assertEquals(201, response.statusCode(),
        "Status HTTP esperado 201 para criação de lead. Payload: {name='" + name + "', email='" + uniqueEmail + "'}. " +
            "Status obtido: " + response.statusCode() + ". Body: " + body);

    assertTrue(body.contains("\"name\":"),
        "Resposta deveria conter o campo 'name'. Payload: {name='" + name + "', email='" + uniqueEmail + "'}. Body: " + body);
    assertTrue(body.contains("\"email\":"),
        "Resposta deveria conter o campo 'email'. Payload: {name='" + name + "', email='" + uniqueEmail + "'}. Body: " + body);
    assertTrue(body.contains(uniqueEmail),
        "Resposta deveria conter o email enviado. Payload: {name='" + name + "', email='" + uniqueEmail + "'}. Body: " + body);
  }

  @Test
  @DisplayName("Deve retornar 409 quando tentar criar lead já existente")
  void shouldReturnConflictWhenLeadAlreadyExists() throws IOException, InterruptedException {
    String email = "duplicado+" + UUID.randomUUID() + "@teste1.com";
    String name = "Lead Duplicado";

    // Primeiro cria com sucesso
    HttpResponse<String> firstResponse = postLead(name, email);
    assertEquals(201, firstResponse.statusCode(),
        "Pré-condição falhou: não foi possível criar o lead inicial. Status: " + firstResponse.statusCode() +
            ", Body: " + firstResponse.body());

    // Depois tenta criar o mesmo lead novamente
    HttpResponse<String> secondResponse = postLead(name, email);
    String secondBody = secondResponse.body();

    assertEquals(408, secondResponse.statusCode(),
        "Status HTTP esperado 409 para lead já existente. Payload: {name='" + name + "', email='" + email + "'}. " +
            "Status obtido: " + secondResponse.statusCode() + ". Body: " + secondBody);
  }

  @Test
  @DisplayName("Deve retornar erro de validação quando nome e email estão vazios")
  void shouldReturnValidationErrorWhenNameAndEmailAreEmpty() throws IOException, InterruptedException {
    String name = "";
    String email = "";
    HttpResponse<String> response = postLead(name, email);

    String body = response.body();

    assertEquals(400, response.statusCode(),
        "Status HTTP esperado 400 para validação. Payload: {name='" + name + "', email='" + email + "'}. " +
            "Status obtido: " + response.statusCode() + ". Body: " + body);
    assertTrue(body.contains("\"message\":\"Validation fails\""),
        "Mensagem global esperada 'Validation fails'. Payload: {name='" + name + "', email='" + email + "'}. Body: " + body);
    assertTrue(body.contains("name is a required field"),
        "Deveria haver erro de validação para name. Payload: {name='" + name + "', email='" + email + "'}. Body: " + body);
    assertTrue(body.contains("email is a required field"),
        "Deveria haver erro de validação para email. Payload: {name='" + name + "', email='" + email + "'}. Body: " + body);
  }

  @Test
  @DisplayName("Deve retornar erro de validação quando nome está vazio")
  void shouldReturnValidationErrorWhenNameIsEmpty() throws IOException, InterruptedException {
    String name = "";
    String email = "teste@teste1.com";
    HttpResponse<String> response = postLead(name, email);

    String body = response.body();

    assertEquals(400, response.statusCode(),
        "Status HTTP esperado 400 para validação. Payload: {name='" + name + "', email='" + email + "'}. " +
            "Status obtido: " + response.statusCode() + ". Body: " + body);
    assertTrue(body.contains("name is a required field"),
        "Deveria haver erro de validação para name. Payload: {name='" + name + "', email='" + email + "'}. Body: " + body);
  }

  @Test
  @DisplayName("Deve retornar erro de validação quando email está vazio")
  void shouldReturnValidationErrorWhenEmailIsEmpty() throws IOException, InterruptedException {
    String name = "Diego Yuri";
    String email = "";
    HttpResponse<String> response = postLead(name, email);

    String body = response.body();

    assertEquals(400, response.statusCode(),
        "Status HTTP esperado 400 para validação. Payload: {name='" + name + "', email='" + email + "'}. " +
            "Status obtido: " + response.statusCode() + ". Body: " + body);
    assertTrue(body.contains("email is a required field"),
        "Deveria haver erro de validação para email. Payload: {name='" + name + "', email='" + email + "'}. Body: " + body);
  }
}
