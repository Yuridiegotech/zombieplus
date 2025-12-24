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
    loginPage.navigate();
    loginPage.submit("admin@zombieplus.com", "pwd123");
    moviesPage.isLoggedIn();

    //cadastrar novo filme
    moviesPage.createNewMovie(
        MoviesData.getStringValue(movie, "title"),
        MoviesData.getStringValue(movie, "overview"),
        MoviesData.getStringValue(movie, "company"),
        MoviesData.getIntegerValue(movie, "release_year")
    );
    components.waitForToastMessageHidden("Cadastro realizado com sucesso!");
    page.waitForTimeout(5000);
  }

  @Test
  @DisplayName("Não deve cadastrar quando os campos obrigatórios não forem preenchidos")
  void pageMoviesRecordWithFieldNull() {

  }


}
