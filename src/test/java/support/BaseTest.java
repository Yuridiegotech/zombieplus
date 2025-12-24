package support;

import com.github.javafaker.Faker;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.zombieplus.factory.BrowserFactory;
import pages.Components;
import pages.LandingPage;
import pages.LoginPage;
import pages.MoviesPage;

public class BaseTest {

    protected BrowserContext context;
    protected Page page;

    // Page Objects - disponíveis para todos os testes
    protected MoviesPage moviesPage;
    protected LoginPage loginPage;
    protected LandingPage landingPage;
    protected Components components;

    // Faker - disponível para todos os testes
    protected Faker faker;

    @BeforeEach
    void setUp() {
        BrowserFactory.headless = false;
        context = BrowserFactory.createContext();
        page = context.newPage();

        // Inicializa todos os Page Objects automaticamente
        moviesPage = new MoviesPage(page);
        loginPage = new LoginPage(page);
        landingPage = new LandingPage(page);
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

