package tests.e2e;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zombieplus.factory.BrowserFactory;
import pages.Components;
import pages.LandingPage;
import pages.LoginPage;

public class LoginTest {

  private BrowserContext context;
  private Page page;
  private LoginPage loginPage;
  private Components components;


  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false;
    context = BrowserFactory.createContext();
    page = context.newPage();
    loginPage = new LoginPage(page);
    components = new Components(page);
    context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
  }

  @Test
  @DisplayName("Deve Logar como Admin")
  void PageLoginAdmin() {

    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "pwd123");
    loginPage.isLoggedIn();



  }

  @Test
  @DisplayName("Não Deve Logar com senha incorreta")
  void PageLoginAdminIncorrectPassword() {

    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "errada");
    String message  = "Ocorreu um erro ao tentar efetuar o login. Por favor, verifique suas credenciais e tente novamente.";
    components.waitForToastMessageHidden(message);

  }

  @Test
  @DisplayName("Não Deve Logar quando o email é inválido")
  void PageLoginAdminEmailWithIncorrect() {

    loginPage.navigate();
    loginPage.submit("yuridiegotech.vercel", "abc123");
    loginPage.assertAlertsTexts("Email incorreto");

  }

  @Test
  @DisplayName("Não Deve Logar quando o email nao é preenchido")
  void PageLoginAdminEmailNull() {

    loginPage.navigate();
    loginPage.submit("", "errada");
    loginPage.assertAlertsTexts("Campo obrigatório");

  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha nao é preenchido")
  void PageLoginAdminPasswordNull() {

    loginPage.navigate();
    loginPage.submit("Teste@teste.com", "");
    loginPage.assertAlertsTexts("Campo obrigatório");

  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha e Email nao é preenchido")
  void PageLoginAdminPasswordAndEmailWithNull() {

    loginPage.navigate();
    loginPage.submit("", "");
    loginPage.assertAlertsTexts("Campo obrigatório","Campo obrigatório");

  }


  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-LoginPage.zip")));
    context.close();
  }

}
