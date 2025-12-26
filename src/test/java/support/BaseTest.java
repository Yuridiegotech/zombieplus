package support;

import com.github.javafaker.Faker;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.zombieplus.factory.BrowserFactory;
import support.actions.Components;
import support.actions.Leads;
import support.actions.Login;
import support.actions.Movies;
import support.api.LeadsApi;
import support.api.MoviesApi;

public class BaseTest {

  protected BrowserContext context;
  protected Page page;
  protected Playwright playwright;

  // Page Objects - disponíveis para todos os testes
  protected Movies movies;
  protected Login login;
  protected Leads leads;
  protected Components components;

  // API - disponível para todos os testes
  protected MoviesApi moviesApi;
  protected LeadsApi leadsApi;

  // Faker - disponível para todos os testes
  protected Faker faker;

  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false;
    playwright = BrowserFactory.getPlaywright();
    context = BrowserFactory.createContext();
    page = context.newPage();

    // Inicializa todos os Page Objects automaticamente
    movies = new Movies(page);
    login = new Login(page);
    leads = new Leads(page);
    components = new Components(page);

    // Inicializa a API
    moviesApi = new MoviesApi(playwright);
    leadsApi = new LeadsApi(playwright);

    // Inicializa o Faker
    faker = new Faker();

    // Inicia o tracing
    context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions()
        .setScreenshots(true)
        .setSnapshots(true)
        .setSources(true));
  }

  @AfterEach
  void tearDown() {
    // Salva o trace com o nome da classe do teste
    String traceName = "trace-" + this.getClass().getSimpleName() + ".zip";
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions()
        .setPath(java.nio.file.Paths.get(traceName)));

    // Fecha recursos
    if (moviesApi != null) {
      moviesApi.dispose();
    }
    if (leadsApi != null) {
      leadsApi.dispose();
    }
    context.close();
  }
}

