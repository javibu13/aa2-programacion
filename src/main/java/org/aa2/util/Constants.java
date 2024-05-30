package org.aa2.util;
import io.github.cdimascio.dotenv.Dotenv;

public class Constants {

    public static final Dotenv dotenv = Dotenv.load();

    public static final String DATABASE = dotenv.get("MYSQL_DATABASE");
    public static final String USERNAME = dotenv.get("MYSQL_USER");
    public static final String PASSWORD = dotenv.get("MYSQL_PASSWORD");

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:" + dotenv.get("MYSQL_PORT") + "/" + DATABASE;

    public static final String DATE_PATTERN = "yyyy-MM-dd";
}
