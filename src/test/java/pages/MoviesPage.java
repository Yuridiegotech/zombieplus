package pages;

import com.microsoft.playwright.Page;
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

}
