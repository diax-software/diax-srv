package me.diax.srv.database.dao;

import com.knockturnmc.api.util.sql.Dao;
import me.diax.srv.stubs.model.Profile;

import java.sql.SQLException;

public interface ProfileDao extends Dao {

    Profile get(long id) throws SQLException;

    void save(Profile profile) throws SQLException;

    Profile getByDiscordId(long discordId) throws SQLException;
}
