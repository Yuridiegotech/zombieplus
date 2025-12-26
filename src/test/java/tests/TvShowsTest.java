package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.BaseTest;
import support.database;
import support.fixtures.tvshows.TvShowsData;

public class TvShowsTest extends BaseTest {

  @Test
  @DisplayName("Deve poder cadastrar uma nova série")
  void pageTvShowsNewRecord() {
    var tvShow = TvShowsData.get("create");

    // Deleta a série do banco de dados caso já exista
    String tvShowTitle = TvShowsData.getStringValue(tvShow, "title");
    database.executeSQL(String.format("DELETE FROM tvshows WHERE title = '" + tvShowTitle + "'"));

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    tvShows.goToPageTvShow();

    page.waitForTimeout(5000);

    //cadastrar nova série
    tvShows.createNewTvShow(
        TvShowsData.getStringValue(tvShow, "title"),
        TvShowsData.getStringValue(tvShow, "overview"),
        TvShowsData.getStringValue(tvShow, "company"),
        TvShowsData.getIntegerValue(tvShow, "release_year"),
        TvShowsData.getIntegerValue(tvShow, "season"),
        TvShowsData.getStringValue(tvShow, "cover"),
        TvShowsData.getBooleanValue(tvShow, "featured")
    );
    components.waitForPopupMessage("A série '" + tvShowTitle + "' foi adicionada ao catálogo.");
  }

  @Test
  @DisplayName("Não deve permitir cadastrar série duplicada")
  void pageTvShowsNewRecordDuplicate() {
    var tvShow = TvShowsData.get("duplicate");

    // Deleta a série do banco de dados caso já exista
    String tvShowTitle = TvShowsData.getStringValue(tvShow, "title");
    database.executeSQL(String.format("DELETE FROM tvshows WHERE title = '" + tvShowTitle + "'"));

    // Insere a série via API (massa de teste)
    tvShowsApi.createTvShow(tvShow);

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    tvShows.goToPageTvShow();

    //cadastrar nova série
    tvShows.createNewTvShow(
        TvShowsData.getStringValue(tvShow, "title"),
        TvShowsData.getStringValue(tvShow, "overview"),
        TvShowsData.getStringValue(tvShow, "company"),
        TvShowsData.getIntegerValue(tvShow, "release_year"),
        TvShowsData.getIntegerValue(tvShow, "season"),
        TvShowsData.getStringValue(tvShow, "cover"),
        TvShowsData.getBooleanValue(tvShow, "featured")
    );

    components.waitForPopupMessage(
        "O título '" + tvShowTitle
            + "' já consta em nosso catálogo. Por favor, verifique se há necessidade de atualizações ou correções para este item.");
  }

  @Test
  @DisplayName("Não deve cadastrar quando os campos obrigatórios não forem preenchidos")
  void pageTvShowsRecordWithFieldNull() {

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //acessar a página de séries
    tvShows.goToPageTvShow();

    tvShows.goForm();
    tvShows.submitForm();
    tvShows.assertAlertsTexts("Campo obrigatório",
        "Campo obrigatório",
        "Campo obrigatório",
        "Campo obrigatório",
        "Campo obrigatório (apenas números)"
    );
  }

  @Test
  @DisplayName("Deve permitir excluir uma série")
  void pageTvShowsDeleteTvShow() {
    var tvShow = TvShowsData.get("delete");

    // Deleta a série do banco de dados caso já exista
    String tvShowTitle = TvShowsData.getStringValue(tvShow, "title");
    database.executeSQL(String.format("DELETE FROM tvshows WHERE title = '" + tvShowTitle + "'"));

    // Insere a série via API (massa de teste)
    tvShowsApi.createTvShow(tvShow);

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //acessar a página de séries
    tvShows.goToPageTvShow();

    //excluir a série
    tvShows.deleteTvShow(tvShowTitle);

    components.waitForPopupMessage("Série removida com sucesso.");
  }

  @Test
  @DisplayName("Deve realizar busca pelo termo zombie")
  void pageTvShowsSearchTvShow() {
    var tvShow = TvShowsData.get("search");

    // Pega o array de séries do JSON
    var tvShowsList = TvShowsData.getTvShowsList(tvShow, "data");

    // Deleta todas as séries do array do banco de dados
    for (var tv : tvShowsList) {
      String tvShowTitle = TvShowsData.getStringValue(tv, "title");
      database.executeSQL(String.format("DELETE FROM tvshows WHERE title = '" + tvShowTitle + "'"));
    }

    // Cria todas as séries via API
    for (var tv : tvShowsList) {
      tvShowsApi.createTvShow(tv);
    }

    //devo estar logado
    login.Login("admin@zombieplus.com", "pwd123", "Admin");

    //acessar a página de séries
    tvShows.goToPageTvShow();
    //realizar a busca
    tvShows.searchTvShow(TvShowsData.getStringValue(tvShow, "input"));

    // Valida que todos os títulos das séries criadas estão nos resultados
    var expectedTitles = tvShowsList.stream()
        .map(tv -> TvShowsData.getStringValue(tv, "title"))
        .toList();
    tvShows.assertSearchResults(expectedTitles);
  }

}
