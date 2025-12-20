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


  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-LoginPage.zip")));
    context.close();
  }

}
