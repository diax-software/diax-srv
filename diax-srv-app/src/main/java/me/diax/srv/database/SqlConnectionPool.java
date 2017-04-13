package me.diax.srv.database;

import com.knockturnmc.api.util.sql.SqlDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
class SqlConnectionPool implements SqlDatasource {

    private final HikariDataSource dataSource;

    @Inject
    public SqlConnectionPool(DatabaseProperties properties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getDatasourceUrl());
        hikariConfig.setUsername(properties.getDatasourceUser());
        hikariConfig.setPassword(properties.getDatasourcePassword());
        hikariConfig.setConnectionInitSql(properties.getConnectionInitSql());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        dataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}