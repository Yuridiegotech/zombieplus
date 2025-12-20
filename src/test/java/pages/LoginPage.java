package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class LoginPage {

  private final Page page;

  public LoginPage(Page page) {
    this.page = page;
  }

  public void navigate() {
    page.navigate("http://localhost:3000/admin/login");
    page.locator(".login-form")
        .isVisible();
  }

  public void submit (String email, String password) {
    page.getByPlaceholder("E-mail")
        .fill(email);
    page.getByPlaceholder("Senha")
        .fill(password);
    page.locator("button[type=\"submit\"]")
        .click();
  }

  public void isLoggedIn() {
    page.waitForLoadState(LoadState.NETWORKIDLE);
    page.waitForURL("**/admin/**");

  }




}
