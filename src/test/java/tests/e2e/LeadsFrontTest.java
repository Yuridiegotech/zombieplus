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

public class LeadsFrontTest {
  private BrowserContext context;
  private Page page;
  private LandingPage landingPage;
  private Components components;

  @BeforeEach
  void setUp() {
    BrowserFactory.headless = false;
    context = BrowserFactory.createContext();
    page = context.newPage();
    landingPage = new LandingPage(page);
    components = new Components(page);

    context.tracing().start(new com.microsoft.playwright.Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
  }

  @Test
  @DisplayName("Deve cadastrar um lead na fila de espera")
  void fluxoPrincipal() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName("Diego Yuri1");
    landingPage.fillEmail("yuridiegotech1@gmail.com");
    landingPage.submitLeadForm();
    String message  = "Agradecemos por compartilhar seus dados conosco. Em breve, nossa equipe entrará em contato!";
    components.waitForToastMessageHidden(message);
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar email invalido")
  void fluxoPrincipalEmailIncorreto() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName("Diego Yuri1");
    landingPage.fillEmail("yuridiegotech1.gmail.com");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Email incorreto");



  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um nome")
  void campoObrigatorioNome() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillEmail("yuridiegotech1@gmail.com");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório");

  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um Email")
  void campoObrigatorioEmail() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName("Diego Yuri1");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório");

  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar nenhum dado")
  void campoObrigatorio() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório", "Campo obrigatório");

  }


  @AfterEach
  void tearDown() {
    // Salva o trace
    context.tracing().stop(new com.microsoft.playwright.Tracing.StopOptions().setPath(java.nio.file.Paths.get("trace-leadsfront.zip")));
    context.close();
  }
}
