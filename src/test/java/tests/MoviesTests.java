package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;
import support.database;
import support.fixtures.movies.MoviesData;

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
    components.waitForPopupMessage("O filme '" + movieTitle + "' foi adicionado ao catálogo.");
  }

  @Test
  @DisplayName("Não deve permitir cadastrar filme duplicado")
  void pageMoviesNewRecordDuplicate() {
    var movie = MoviesData.get("duplicate");

    // Deleta o filme do banco de dados caso já exista
    String movieTitle = MoviesData.getStringValue(movie, "title");
    database.executeSQL(String.format("DELETE FROM movies WHERE title = '" + movieTitle + "'"));

    // Insere o filme via API (massa de teste)
    moviesApi.createMovie(movie);

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

    components.waitForPopupMessage(
        "O título '" + movieTitle
            + "' já consta em nosso catálogo. Por favor, verifique se há necessidade de atualizações ou correções para este item.");
  }

  @Test
  @DisplayName("Não deve cadastrar quando os campos obrigatórios não forem preenchidos")
  void pageMoviesRecordWithFieldNull() {

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");
    movies.goForm();
    movies.submitForm();
    movies.assertAlertsTexts("Campo obrigatório",
        "Campo obrigatório",
        "Campo obrigatório",
        "Campo obrigatório"
    );
  }

  @Test
  @DisplayName("Deve permitir excluir um filme")
  void pageMoviesDeleteMovie() {
    var movie = MoviesData.get("delete");

    // Deleta o filme do banco de dados caso já exista
    String movieTitle = MoviesData.getStringValue(movie, "title");
    database.executeSQL(String.format("DELETE FROM movies WHERE title = '" + movieTitle + "'"));

    // Insere o filme via API (massa de teste)
    moviesApi.createMovie(movie);

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //excluir o filme
    movies.deleteMovie(movieTitle);

    components.waitForPopupMessage(
        "Filme removido com sucesso.");
  }

  @Test
  @DisplayName("Deve realizar busca pelo termo zumbi")
  void pageMoviesSearchMovie() {
    var movie = MoviesData.get("search");

    // Pega o array de filmes do JSON
    var moviesList = MoviesData.getMoviesList(movie, "data");

    // Deleta todos os filmes do array do banco de dados
    for (var m : moviesList) {
      String movieTitle = MoviesData.getStringValue(m, "title");
      database.executeSQL(String.format("DELETE FROM movies WHERE title = '" + movieTitle + "'"));
    }

    // Cria todos os filmes via API
    for (var m : moviesList) {
      moviesApi.createMovie(m);
    }

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //realizar a busca
    movies.searchMovie(MoviesData.getStringValue(movie, "input"));

    // Valida que todos os títulos dos filmes criados estão nos resultados
    var expectedTitles = moviesList.stream()
        .map(m -> MoviesData.getStringValue(m, "title"))
        .toList();
    movies.assertSearchResults(expectedTitles);
  }

}
