package tests.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;
import support.database;
import support.fixtures.MoviesData;

public class MoviesTests extends BaseTest {

  @Test
  @DisplayName("Deve poder cadastrar um novo Filme")
  void pageMoviesNewRecord() {
    var movie = MoviesData.get("create");

    // Deleta o filme do banco de dados caso já exista
    String movieTitle = MoviesData.getStringValue(movie, "title");
    database.executeSQL(String.format("DELETE FROM movies WHERE title = '" + movieTitle + "'"));

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //cadastrar novo filme
    movies.createNewMovie(
        MoviesData.getStringValue(movie, "title"),
        MoviesData.getStringValue(movie, "overview"),
        MoviesData.getStringValue(movie, "company"),
        MoviesData.getIntegerValue(movie, "release_year"),
        MoviesData.getStringValue(movie, "cover"),
        MoviesData.getBooleanValue(movie, "featured")
    );
    components.waitForToastMessageHidden("Cadastro realizado com sucesso!");
    page.waitForTimeout(5000);
  }

  @Test
  @DisplayName("Não deve cadastrar quando os campos obrigatórios não forem preenchidos")
  void pageMoviesRecordWithFieldNull() {

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");
    movies.goForm();
    movies.submitForm();
    movies.assertAlertsTexts(
        "Por favor, informe o título.",
        "Por favor, informe a sinopse.",
        "Por favor, informe a empresa distribuidora.",
        "Por favor, informe o ano de lançamento."
    );
  }


}
