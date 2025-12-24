package support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class database {

    // Configuração do banco de dados
    private static final String DB_HOST = "localhost";
    private static final String DB_NAME = "zombieplus";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "pwd123";
    private static final String DB_PORT = "5432";

    private static String getConnectionUrl() {
        return String.format("jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
    }

    public static void executeSQL(String sqlScript) {
        try (Connection connection = DriverManager.getConnection(getConnectionUrl(), DB_USER, DB_PASS);
             Statement statement = connection.createStatement()) {

            int rowsAffected = statement.executeUpdate(sqlScript);
            System.out.println("SQL executado com sucesso. Linhas afetadas: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Erro ao executar SQL: " + e.getMessage());
            throw new RuntimeException("Erro ao executar SQL: " + e.getMessage(), e);
        }
    }
}
