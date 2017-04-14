package me.diax.srv.database.connector;

import com.knockturnmc.api.util.sql.SqlConnector;
import com.knockturnmc.api.util.sql.SqlDatasource;
import me.diax.srv.database.dao.ProfileDao;
import me.diax.srv.stubs.model.Profile;

import javax.inject.Inject;
import java.sql.*;

class ProfileConnector extends SqlConnector implements ProfileDao {

    @Inject
    protected ProfileConnector(SqlDatasource datasource) {
        super(datasource);
    }

    @Override
    public Profile get(long id) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT xp FROM profile WHERE id = ?;"
            )) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return null;
                }

                Profile profile = new Profile();
                profile.setId(id);
                profile.setXp(rs.getLong("xp"));

                return profile;
            }
        }
    }

    @Override
    public void save(Profile profile) throws SQLException {
        if (profile.isNew()) {
            throw new IllegalArgumentException("Cannot save profile without an ID");
        }
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO profile (id, xp) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE xp = ?;"
            )) {
                ps.setLong(1, profile.getId());

                ps.setLong(2, profile.getXp());
                ps.setLong(3, profile.getXp());
                ps.execute();
            }
        }
    }
}
