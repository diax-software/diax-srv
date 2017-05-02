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
                    "SELECT profile.id, xp, balance, discordid " +
                            "FROM profile " +
                            "RIGHT OUTER JOIN profile_discord ON profile_discord.id = profile.id " +
                            "WHERE profile.id = ?;"
            )) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                return renderProfile(rs);
            }
        }
    }

    @Override
    public Profile getByDiscordId(long discordId) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT profile.id, balance, xp, discordid " +
                            "FROM profile_discord " +
                            "JOIN profile ON profile_discord.id = profile.id " +
                            "WHERE profile_discord.discordid = ?;"
            )) {
                ps.setLong(1, discordId);
                ResultSet rs = ps.executeQuery();
                return renderProfile(rs);
            }
        }
    }

    @Override
    public void save(Profile profile) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "CALL proc_save_profile(?, ?, ?, ?);"
            )) {
                if (profile.getId() != null) {
                    ps.setLong(1, profile.getId());
                } else {
                    ps.setNull(1, Types.BIGINT);
                }
                ps.setLong(2, profile.getXp());
                ps.setLong(3, profile.getBalance());

                if (profile.getDiscordId() != null) {
                    ps.setLong(4, profile.getDiscordId());
                } else {
                    ps.setNull(4, Types.BIGINT);
                }
                ps.execute();
            }
        }
    }

    /**
     * Renders a profile using the ResultSet that the method {@link #get(long)} uses.
     *
     * @param rs the resultset right after querying
     * @return the Profile or {@code null} if no profile was found
     */
    private Profile renderProfile(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        Profile profile = new Profile();
        profile.setId(rs.getLong("id"));
        profile.setXp(rs.getLong("xp"));
        profile.setBalance(rs.getLong("balance"));

        profile.setDiscordId(rs.getLong("discordid"));
        if (rs.wasNull()) {
            profile.setDiscordId(null);
        }

        return profile;
    }

}
