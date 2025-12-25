package tests.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;

public class LoginTest extends BaseTest {

  @Test
  @DisplayName("Deve Logar como Admin")
  void PageLoginAdmin() {
    login.navigate();
    login.submit("admin@zombieplus.com", "pwd123");
    login.isLoggedIn();
  }

  @Test
  @DisplayName("Não Deve Logar com senha incorreta")
  void PageLoginAdminIncorrectPassword() {
    login.navigate();
    login.submit("admin@zombieplus.com", "errada");
    String message = "Ocorreu um erro ao tentar efetuar o login. Por favor, verifique suas credenciais e tente novamente.";
    components.waitForToastMessageHidden(message);
  }

  @Test
  @DisplayName("Não Deve Logar quando o email é inválido")
  void PageLoginAdminEmailWithIncorrect() {
    login.navigate();
    login.submit("yuridiegotech.vercel", "abc123");
    login.assertAlertsTexts("Email incorreto");
  }

  @Test
  @DisplayName("Não Deve Logar quando o email nao é preenchido")
  void PageLoginAdminEmailNull() {
    login.navigate();
    login.submit("", "errada");
    login.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha nao é preenchido")
  void PageLoginAdminPasswordNull() {
    login.navigate();
    login.submit("Teste@teste.com", "");
    login.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Não Deve Logar quando o Senha e Email nao é preenchido")
  void PageLoginAdminPasswordAndEmailWithNull() {
    login.navigate();
    login.submit("", "");
    login.assertAlertsTexts("Campo obrigatório", "Campo obrigatório");
  }

}
