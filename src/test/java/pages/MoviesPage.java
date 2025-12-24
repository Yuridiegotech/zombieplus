package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.FilterOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

public class MoviesPage {

  private final Page page;

  public MoviesPage(Page page) {
    this.page = page;
  }

  public void isLoggedIn() {
    page.waitForLoadState(LoadState.NETWORKIDLE);
    page.waitForURL("**/admin/**");
  }

  public void createNewMovie(String title, String overview, String companyName, String release_year) {

    page.locator("a[href$=\"register\"]").click();

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

    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cadastrar")).click();
    


  }

}
