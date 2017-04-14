package me.diax.srv.database;

import com.knockturnmc.api.util.sql.SqlDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.diax.srv.stubs.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
class SqlConnectionPool implements SqlDatasource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
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

        updateDatabase(properties);
    }

    private void updateDatabase(DatabaseProperties properties) {
        ServiceInstaller database = new ServiceInstaller(this);
        Version installed = database.getVersion();
        Version newest = database.getNewest();

        if (installed == null) {
            logger.info("Database appears not to be installed");
            database.updateSchema();
            logger.info("Installed version of database: " + database.getVersion());
        } else {
            switch (installed.compareTo(newest)) {
                case -1:
                    logger.info("Older version of database installed: " + database.getVersion());
                    if (properties.isUsingAutoUpdate()) {
                        database.updateSchema();
                        logger.info("Installed version of database: " + database.getVersion());
                    } else {
                        logger.info("Newer version of database available: " + database.getNewest());
                    }
                    break;
                case 0:
                    logger.info("Latest version of database installed: " + database.getVersion());
                    break;
                case 1:
                default:
                    logger.warn("Version mismatch: newer unknown version of database is installed");
                    logger.warn("Installed version: " + database.getVersion());
                    logger.warn("Available version: " + database.getNewest());
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}