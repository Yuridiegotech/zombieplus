// src/main/java/org/zombieplus/factory/BrowserFactory.java
package org.zombieplus.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

  public static boolean headless = true;
  public static boolean debug = false;
  public static boolean video = false;

  private static Playwright playwright;
  private static Browser browser;

  public static BrowserContext createContext() {
    if (playwright == null) {
      playwright = Playwright.create();
    }
    if (browser == null) {
      browser = playwright.chromium().launch(
          new BrowserType.LaunchOptions().setHeadless(headless)
      );
    }
    return browser.newContext();
  }

  public static void close() {
    if (playwright != null) {
      playwright.close();
    }
  }
}