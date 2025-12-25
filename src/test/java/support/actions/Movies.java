package support.actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.nio.file.Paths;

public class Movies {

  private final Page page;

  public Movies(Page page) {
    this.page = page;
  }


  public void goForm() {
    page.locator("a[href$=\"register\"]").click();
  }

  public void submitForm() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cadastrar")).click();
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

  public void createNewMovie(String title, String overview, String companyName,
      String release_year, String cover, boolean featured) {

    goForm();
    page.getByLabel("Titulo do filme").fill(title);
    page.getByLabel("Sinopse").fill(overview);
    //Seleciona Companhia
    page.locator("#select_company_id .react-select__indicator").click();
    page.locator(".react-select__option").filter(new FilterOptions().setHasText(companyName))
        .click();
    //Seleciona Ano de lançamento
    page.locator("#select_year .react-select__indicator").click();
    page.locator(".react-select__option").filter(new FilterOptions().setHasText(release_year))
        .click();

    page.locator("input[name=cover]")
        .setInputFiles(Paths.get("src/test/java/support/fixtures" + cover));

    if (featured) {
      page.locator(".featured .react-switch").click();
    }

    submitForm();
  }

}
