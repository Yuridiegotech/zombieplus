package tests.e2e;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zombieplus.factory.BrowserFactory;
import pages.Components;
import pages.LoginPage;
import pages.MoviesPage;
import fixtures.MoviesData;

public class MoviesTests {

  private BrowserContext context;
  private Page page;
  private MoviesPage moviesPage;
  private LoginPage loginPage;
  private Components components;


  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false;
    context = BrowserFactory.createContext();
    page = context.newPage();
    moviesPage = new MoviesPage(page);
    loginPage = new LoginPage(page);
    components = new Components(page);
    context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
  }

  @Test
  @DisplayName("Deve poder cadastrar um novo Filme")
  void pageMoviesNewRecord() {
    var movie = MoviesData.get("create");

    //devo estar logado
    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "pwd123");
    moviesPage.isLoggedIn();

    //cadastrar novo filme
    moviesPage.createNewMovie(
        MoviesData.getStringValue(movie, "title"),
        MoviesData.getStringValue(movie, "overview"),
        MoviesData.getStringValue(movie, "company"),
        MoviesData.getIntegerValue(movie, "release_year")
    );
    components.waitForToastMessageHidden("Cadastro realizado com sucesso!");
    page.waitForTimeout(5000);
  }


  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-MoviesPage.zip")));
    context.close();
  }

}
