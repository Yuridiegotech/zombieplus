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

    leads.navigate();
    leads.openLeadModal();
    leads.fillName(leadName);
    leads.fillEmail(leadEmail);
    leads.submitLeadForm();
    String message = "Agradecemos por compartilhar seus dados conosco. Em breve, nossa equipe entrará em contato!";
    components.waitForToastMessageHidden(message);
  }

  @Test
  @DisplayName("Deve cadastrar um lead na fila de espera")
  void fluxoPrincipal1() {
    page.navigate("https://google.com.br");
    page.waitForTimeout(5000);
  }


  @Test
  @DisplayName("Não deve cadastrar quando um email já existe")
  void FluxoPrincipalNaoCadastroEmailExistente() {
    String leadName = faker.name().fullName();
    String leadEmail = faker.internet().emailAddress();

    // Cadastra o lead via API primeiro (massa de teste)
    var newLead = page.request().post("http://localhost:3333/leads", RequestOptions.create()
        .setData(java.util.Map.of(
            "name", leadName,
            "email", leadEmail
        )));
    assert newLead.status() == 201 : "Falha ao cadastrar lead via API";
    // Tenta cadastrar o mesmo lead via UI
    leads.navigate();
    leads.openLeadModal();
    leads.fillName(leadName);
    leads.fillEmail(leadEmail);
    leads.submitLeadForm();
    String message2 = "O endereço de e-mail fornecido já está registrado em nossa fila de espera.";
    components.waitForToastMessageHidden(message2);
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar email invalido")
  void fluxoPrincipalEmailIncorreto() {
    leads.navigate();
    leads.openLeadModal();
    leads.fillName("Diego Yuri1");
    leads.fillEmail("yuridiegotech1.gmail.com");
    leads.submitLeadForm();
    leads.assertAlertsTexts("Email incorreto");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um nome")
  void campoObrigatorioNome() {
    leads.navigate();
    leads.openLeadModal();
    leads.fillEmail("yuridiegotech1@gmail.com");
    leads.submitLeadForm();
    leads.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar um Email")
  void campoObrigatorioEmail() {
    leads.navigate();
    leads.openLeadModal();
    leads.fillName("Diego Yuri1");
    leads.submitLeadForm();
    leads.assertAlertsTexts("Campo obrigatório");
  }

  @Test
  @DisplayName("Deve retornar erro ao cadastrar sem enviar nenhum dado")
  void campoObrigatorio() {
    leads.navigate();
    leads.openLeadModal();
    leads.submitLeadForm();
    leads.assertAlertsTexts("Campo obrigatório", "Campo obrigatório");
  }

}
