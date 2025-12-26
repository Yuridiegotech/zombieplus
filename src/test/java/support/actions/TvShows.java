package support.actions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import java.nio.file.Paths;

public class TvShows {

  private final Page page;

  public TvShows(Page page) {
    this.page = page;
  }

  public void goForm() {
    page.locator("a[href=\"/admin/tvshows/register\"]").click();
  }

  public void submitForm() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cadastrar")).click();
  }

  public void assertAlertsTexts(String... expectedTexts) {
    Locator alerts = page.locator(".alert");
    int count = alerts.count();
    assert count == expectedTexts.length : "Quantidade de alertas diferente do esperado";
    for (int i = 0; i < count; i++) {
      alerts.nth(i).waitFor();
      String actual = alerts.nth(i).textContent();
      assert actual.contains(expectedTexts[i]) :
          "Esperado: " + expectedTexts[i] + ", Obtido: " + actual;
    }
  }

  public void createNewTvShow(String title, String overview, String companyName,
      String releaseYear, String season, String cover, boolean featured) {

    goForm();
    page.getByLabel("Titulo da série").fill(title);
    page.getByLabel("Sinopse").fill(overview);

    //Seleciona Companhia
    page.locator("#select_company_id .react-select__indicator").click();
    page.locator(".react-select__option").filter(new FilterOptions().setHasText(companyName))
        .click();

    //Seleciona Ano de lançamento
    page.locator("#select_year .react-select__indicator").click();
    page.locator(".react-select__option").filter(new FilterOptions().setHasText(releaseYear))
        .click();

    //Preenche temporadas
    page.getByLabel("Temporadas").fill(season);

    page.locator("input[name=cover]")
        .setInputFiles(Paths.get("src/test/java/support/fixtures/" + cover));

    if (featured) {
      page.locator(".featured .react-switch").click();
    }

    submitForm();
  }

  public void deleteTvShow(String tvShowTitle) {
    page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(tvShowTitle))
        .getByRole(AriaRole.BUTTON)
        .click();

    page.locator(".confirm-removal").click();
  }

  public void searchTvShow(String target) {
    page.getByPlaceholder("Busque pelo nome").fill(target);
    page.click(".actions button");
  }

  public void assertSearchResults(java.util.List<String> expectedTitles) {
    // Aguarda a primeira linha aparecer
    page.locator("td.title").first().waitFor();

    // Pega apenas os títulos, excluindo o texto do <small>
    Locator titleCells = page.locator("td.title");
    java.util.List<String> foundTitles = new java.util.ArrayList<>();

    int count = titleCells.count();
    for (int i = 0; i < count; i++) {
      // Usa evaluate para pegar apenas os childNodes de texto, ignorando o <small>
      String title = (String) titleCells.nth(i).evaluate(
          "element => Array.from(element.childNodes)" +
              ".filter(node => node.nodeType === Node.TEXT_NODE)" +
              ".map(node => node.textContent.trim())" +
              ".join(' ')"
      );
      foundTitles.add(title);
    }

    System.out.println("===================================================");
    System.out.println("VALIDANDO RESULTADOS DA BUSCA");
    System.out.println("===================================================");
    System.out.println("Títulos localizados (td.title): " + foundTitles);
    System.out.println("Títulos esperados (tvshow.title): " + expectedTitles);
    System.out.println("===================================================\n");

    // Valida que cada título esperado está contido na lista de títulos encontrados
    for (String expectedTitle : expectedTitles) {
      System.out.println("✓ Validando: " + expectedTitle);
      assert foundTitles.contains(expectedTitle) :
          "TV Show não encontrado nos resultados da busca: " + expectedTitle +
              "\nTítulos encontrados: " + foundTitles;
    }

    System.out.println(
        "\n✓ Todos os " + expectedTitles.size() + " TV Shows foram encontrados na busca\n");
  }

  public void goToPageTvShow() {
    page.locator("a[href$='/tvshows']").click();
    page.waitForURL("**/admin/tvshows");
    page.waitForLoadState(LoadState.NETWORKIDLE);
  }


}

