package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;
    private static HikariConfig hikariConfig;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getenv("JDBC_URL"));
            config.setUsername("root");
            config.setPassword(System.getenv("databasepassword"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            config.setMaximumPoolSize(12);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            hikariConfig = config;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing DataSource", e);
        }
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null && hikariConfig != null) {
            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource;
    }
}
