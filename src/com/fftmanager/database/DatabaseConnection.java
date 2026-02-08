package com.fftmanager.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String DB_URL_PROP = "db.url";
    private static final String DB_USER_PROP = "db.user";
    private static final String DB_PASSWORD_PROP = "db.password";

    private static String url;
    private static String user;
    private static String password;

    static {
        loadCredentials();
    }

    private static void loadCredentials() {
        Path configPath = Paths.get(System.getProperty("user.dir"), "database.properties");

        try (FileInputStream input = new FileInputStream(configPath.toFile())) {
            Properties props = new Properties();
            props.load(input);
            url = props.getProperty(DB_URL_PROP);
            user = props.getProperty(DB_USER_PROP);
            password = props.getProperty(DB_PASSWORD_PROP);
        } catch (IOException e) {
            throw new IllegalStateException(
                "Arquivo database.properties não encontrado na raiz do projeto. " +
                "Copie database.properties.example como referência."
            );
        }

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                "database.properties incompleto. Configure db.url, db.user e db.password."
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
