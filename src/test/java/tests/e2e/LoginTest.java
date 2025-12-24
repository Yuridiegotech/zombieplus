package tests.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;

public class LoginTest extends BaseTest {

  @Test
  @DisplayName("Deve Logar como Admin")
  void PageLoginAdmin() {
    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "pwd123");
    moviesPage.isLoggedIn();
  }

  @Test
  @DisplayName("Não Deve Logar com senha incorreta")
  void PageLoginAdminIncorrectPassword() {
    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "errada");
    String message  = "Ocorreu um erro ao tentar efetuar o login. Por favor, verifique suas credenciais e tente novamente.";
    components.waitForToastMessageHidden(message);
  }

  @Test
  @DisplayName("Não Deve Logar quando o email é inválido")
  void PageLoginAdminEmailWithIncorrect() {
    loginPage.navigate();
    loginPage.submit("yuridiegotech.vercel", "abc123");
    loginPage.assertAlertsTexts("Email incorreto");
  }

  @Test
  @DisplayName("Não Deve Logar quando o email nao é preenchido")
  void PageLoginAdminEmailNull() {
    loginPage.navigate();
    loginPage.submit("", "errada");
    loginPage.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha nao é preenchido")
  void PageLoginAdminPasswordNull() {
    loginPage.navigate();
    loginPage.submit("Teste@teste.com", "");
    loginPage.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha e Email nao é preenchido")
  void PageLoginAdminPasswordAndEmailWithNull() {
    loginPage.navigate();
    loginPage.submit("", "");
    loginPage.assertAlertsTexts("Campo obrigatório","Campo obrigatório");
  }

}
