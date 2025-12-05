package tests;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.WaitForOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zombieplus.factory.BrowserFactory;

public class LeadsFrontTest {
  private BrowserContext context;
  private Page page;

  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false; // exemplo: rodar com interface
    context = BrowserFactory.createContext();
    page = context.newPage();
    // Inicia o trace
    context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
  }

  @Test
  @DisplayName("Deve cadastrar um lead na fila de espera")
  void fluxoPrincipal() {
    page.navigate("http://localhost:3000");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();
    page.getByPlaceholder("Seu nome completo").fill("Diego Yuri1");
    page.getByPlaceholder("Seu email principal").fill("yuridiegotech1@gmail.com");
    page.waitForTimeout(1000); // espera 1 segundo
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();

    String message  = "Agradecemos por compartilhar seus dados conosco. Em breve, nossa equipe entrará em contato!";
    page.locator(".toast")
        .getByText(message)
        .waitFor(new Locator.WaitForOptions()
        .setState(WaitForSelectorState.HIDDEN)
        .setTimeout(5000)
    );

  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar email invalido")
  void fluxoPrincipalEmailIncorreto() {
    page.navigate("http://localhost:3000");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();
    page.getByPlaceholder("Seu nome completo").fill("Diego Yuri1");
    page.getByPlaceholder("Seu email principal").fill("yuridiegotech.gmail.com");
    page.waitForTimeout(1000); // espera 1 segundo
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();

    Locator alertaEmail = page.locator("input[name='email'] + .alert");
    alertaEmail.waitFor();
    assert (alertaEmail.textContent().equals("Email incorreto"));



  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um nome")
  void campoObrigatorioNome() {
    page.navigate("http://localhost:3000");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();
    page.getByPlaceholder("Seu email principal").fill("yuridiegotech.gmail.com");
    page.waitForTimeout(1000); // espera 1 segundo
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();

    Locator alertaNome = page.locator("input[name='name'] + .alert");
    alertaNome.waitFor();
    assert (alertaNome.textContent().contains("Campo obrigatório"));

  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um Email")
  void campoObrigatorioEmail() {
    page.navigate("http://localhost:3000");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();

    page.getByPlaceholder("Seu nome completo").fill("Diego Yuri1");
    page.waitForTimeout(1000); // espera 1 segundo
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();

    Locator alertaEmail = page.locator("input[name='email'] + .alert");
    alertaEmail.waitFor();
    assert (alertaEmail.textContent().contains("Campo obrigatório"));

  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar nenhum dado")
  void campoObrigatorio() {
    page.navigate("http://localhost:3000");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();

    page.waitForTimeout(1000); // espera 1 segundo
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();

    Locator alertaNome = page.locator("input[name='name'] + .alert");
    alertaNome.waitFor();
    assert (alertaNome.textContent().contains("Campo obrigatório"));

    Locator alertaEmail = page.locator("input[name='email'] + .alert");
    alertaEmail.waitFor();
    assert (alertaEmail.textContent().contains("Campo obrigatório"));

  }


  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-leadsfront.zip")));
    context.close();
  }
}
