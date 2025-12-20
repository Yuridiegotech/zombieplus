package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Components {

  private final Page page;

  public Components(Page page) {
    this.page = page;
  }

  public void waitForToastMessageHidden(String message) {
    page.locator(".toast").getByText(message)
        .waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
        );
    page.waitForTimeout(5000);
    page.locator(".toast").getByText(message)
        .waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.HIDDEN)
        );
  }

}
