package actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class Leads {

  private final Page page;

  public Leads(Page page) {
    this.page = page;
  }

  public void navigate() {
    page.navigate("http://localhost:3000");
  }

  public void openLeadModal() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Aperte o play")).click();
    page.getByTestId("modal")
        .getByRole(AriaRole.HEADING)
        .filter(new Locator.FilterOptions().setHasText("Fila de espera"))
        .waitFor();
  }

  public void fillName(String name) {
    page.getByPlaceholder("Informe seu nome").fill(name);
  }

  public void fillEmail(String email) {
    page.getByPlaceholder("Informe seu email").fill(email);
  }

  public void submitLeadForm() {
    page.waitForTimeout(1000);
    page.getByTestId("modal")
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Quero entrar na fila!"))
        .click();
  }


  public void assertAlertsTexts(String... expectedTexts) {
    Locator alerts = page.locator(".alert");
    int count = alerts.count();
    assert count == expectedTexts.length : "Quantidade de alertas diferente do esperado";
    for (int i = 0; i < count; i++) {
      alerts.nth(i).waitFor(); // Aguarda cada alerta individualmente
      String actual = alerts.nth(i).textContent();
      assert actual.contains(expectedTexts[i]) :
          "Esperado: " + expectedTexts[i] + ", Obtido: " + actual;
    }
  }
}
