package com.unieus.garajea.model.infraestructure;

import java.util.Properties;

public class InfraConfig {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public InfraConfig(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public static InfraConfig from(Properties p) {
        return new InfraConfig(p.getProperty("db.url"),
                p.getProperty("db.user"),
                p.getProperty("db.password"));
    }
}
