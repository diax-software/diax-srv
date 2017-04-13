package me.diax.srv.database;

import com.knockturnmc.api.util.sql.SqlDatasource;
import me.diax.srv.stubs.model.Version;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Utility for installing scripts onto an SQL server
 *
 * @author Sven Olderaan (admin@heaven-craft.net)
 */
@Singleton
public class ServiceInstaller {

    private static final String SCHEMA_DIR = "schema";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TreeMap<Version, String> sqlSchema;
    private final SqlDatasource datasource;

    /**
     * Creates a new Service Installer
     *
     * @param datasource the datasource
     */
    @Inject
    ServiceInstaller(SqlDatasource datasource) {
        this.datasource = datasource;
        Reflections reflections = new Reflections(SCHEMA_DIR, new ResourcesScanner());
        Set<String> properties = reflections.getResources(Pattern.compile(".*\\.sql"));
        sqlSchema = new TreeMap<>();
        properties.forEach(s -> {
            if (s.startsWith(SCHEMA_DIR)) {
                sqlSchema.put(new Version(s.replace(SCHEMA_DIR + "/", "").replace(".sql", "")), "/" + s);
            }
        });
    }

    /**
     * Gets the version of the database that is currently installed
     *
     * @return the version of the database
     */
    private Version getVersion() {
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT value FROM system WHERE setting = 'version';"
            )) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Version(rs.getString("value"));
                }
            }
        } catch (SQLException ignored) {

        }
        return null;
    }

    /**
     * Runs all DDL scripts that are bundled with the application
     *
     * @return a boolean to indicate success
     */
    public boolean updateSchema() {
        try (Connection connection = datasource.getConnection()) {
            for (Map.Entry<Version, String> script : sqlSchema.entrySet()) {
                Version installedVersion = getVersion();
                Version scriptVerson = script.getKey();
                if (installedVersion != null && scriptVerson.compareTo(installedVersion) <= 0) {
                    continue;
                }
                logger.info("Installing database script: " + script.getValue());
                try (InputStreamReader inputStream =
                             new InputStreamReader(getClass().getResourceAsStream(script.getValue()))) {
                    ScriptRunner runner = new ScriptRunner(connection, false, true);
                    runner.runScript(inputStream);
                }
                try (PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO system (setting, value) " +
                                "VALUES ('version', ?) " +
                                "ON DUPLICATE KEY UPDATE value = ?;"
                )) {
                    ps.setString(1, scriptVerson.getVersion());
                    ps.setString(2, scriptVerson.getVersion());
                    ps.execute();
                }
            }
            logger.info("Latest version of database installed: " + getVersion());
            return true;
        } catch (SQLException | IOException e) {
            logger.error("Failed to install.", e);
        }
        return false;
    }
}
