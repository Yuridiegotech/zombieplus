package actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class Login {

  private final Page page;

  public Login(Page page) {
    this.page = page;
  }

  public void navigate() {
    page.navigate("http://localhost:3000/admin/login");
    page.locator(".login-form")
        .isVisible();
  }

  public void submit(String email, String password) {
    page.getByPlaceholder("E-mail")
        .fill(email);
    page.getByPlaceholder("Senha")
        .fill(password);
    page.locator("button[type=\"submit\"]")
        .click();
  }


  public void assertAlertsTexts(String... expectedTexts) {
    Locator alerts = page.locator("span[class$=alert]");
    int count = alerts.count();
    assert count == expectedTexts.length : "Quantidade de alertas diferente do esperado";
    for (int i = 0; i < count; i++) {
      alerts.nth(i).waitFor(); // Aguarda cada alerta individualmente
      String actual = alerts.nth(i).textContent();
      assert actual.contains(expectedTexts[i]) :
          "Esperado: " + expectedTexts[i] + ", Obtido: " + actual;
    }
  }


  public void isLoggedIn() {
    page.waitForLoadState(LoadState.NETWORKIDLE);
    page.waitForURL("**/admin/**");
  }


}
