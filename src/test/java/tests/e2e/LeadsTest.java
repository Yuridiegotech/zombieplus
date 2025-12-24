package tests.e2e;

import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;

public class LeadsTest extends BaseTest {

  @Test
  @DisplayName("Deve cadastrar um lead na fila de espera")
  void fluxoPrincipal() {
    String leadName = faker.name().fullName();
    String leadEmail = faker.internet().emailAddress();

    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName(leadName);
    landingPage.fillEmail(leadEmail);
    landingPage.submitLeadForm();
    String message  = "Agradecemos por compartilhar seus dados conosco. Em breve, nossa equipe entrará em contato!";
    components.waitForToastMessageHidden(message);
  }

  @Test
  @DisplayName("Não deve cadastrar quando um email já existe")
  void FluxoPrincipalNaoCadastroEmailExistente() {
    String leadName = faker.name().fullName();
    String leadEmail = faker.internet().emailAddress();

    // Cadastra o lead via API primeiro (massa de teste)
    var newLead = page.request().post("http://localhost:3333/leads",RequestOptions.create()
            .setData(java.util.Map.of(
                "name", leadName,
                "email", leadEmail
            )));
    assert newLead.status() == 201 : "Falha ao cadastrar lead via API";
    // Tenta cadastrar o mesmo lead via UI
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName(leadName);
    landingPage.fillEmail(leadEmail);
    landingPage.submitLeadForm();
    String message2  = "O endereço de e-mail fornecido já está registrado em nossa fila de espera.";
    components.waitForToastMessageHidden(message2);
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar email invalido")
  void fluxoPrincipalEmailIncorreto() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName("Diego Yuri1");
    landingPage.fillEmail("yuridiegotech1.gmail.com");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Email incorreto");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um nome")
  void campoObrigatorioNome() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillEmail("yuridiegotech1@gmail.com");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um Email")
  void campoObrigatorioEmail() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.fillName("Diego Yuri1");
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar nenhum dado")
  void campoObrigatorio() {
    landingPage.navigate();
    landingPage.openLeadModal();
    landingPage.submitLeadForm();
    landingPage.assertAlertsTexts("Campo obrigatório", "Campo obrigatório");
  }

}
