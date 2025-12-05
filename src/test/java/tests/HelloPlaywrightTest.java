package tests;
import com.microsoft.playwright.*;
import org.zombieplus.factory.BrowserFactory;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HelloPlaywrightTest {
  private BrowserContext context;
  private Page page;

  @BeforeEach
  void setUp() {
    context = BrowserFactory.createContext();
    page = context.newPage();
    // Inicia o trace
    context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
  }

  @Test
  void openPlaywrightWebsite() {
    page.navigate("https://playwright.dev/");
    String title = page.title();
    assertTrue(title.contains("Playwright"));
  }

  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-hello.zip")));
    context.close();
  }
}