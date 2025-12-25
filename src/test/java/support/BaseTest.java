package support;

import com.github.javafaker.Faker;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.zombieplus.factory.BrowserFactory;
import actions.Components;
import actions.Leads;
import actions.Login;
import actions.Movies;

public class BaseTest {

  protected BrowserContext context;
  protected Page page;

  // Page Objects - disponíveis para todos os testes
  protected Movies movies;
  protected Login login;
  protected Leads leads;
  protected Components components;

  // Faker - disponível para todos os testes
  protected Faker faker;

  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false;
    context = BrowserFactory.createContext();
    page = context.newPage();

    // Inicializa todos os Page Objects automaticamente
    movies = new Movies(page);
    login = new Login(page);
    leads = new Leads(page);
    components = new Components(page);

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
    context.close();
  }
}

