ALTER TABLE profile
  MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

DROP TABLE IF EXISTS profile_discord;
CREATE TABLE profile_discord (
  id        BIGINT NOT NULL,
  discordid BIGINT NOT NULL,
  CONSTRAINT pk_profile_discord PRIMARY KEY (id),
  CONSTRAINT fk_profile_discord_discordid FOREIGN KEY (discordid) REFERENCES profile (id),
  CONSTRAINT un_profile_discord_discordid UNIQUE INDEX (discordid)
)
  ENGINE = InnoDB;

DROP PROCEDURE IF EXISTS proc_save_profile;

DELIMITER //
CREATE PROCEDURE proc_save_profile(
  IN _id BIGINT, _xp BIGINT, _balance BIGINT, IN _discordid BIGINT
) BEGIN

  DECLARE CONTINUE HANDLER FOR SQLSTATE '45001'
  BEGIN
    ROLLBACK;
  END;
  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
  END;

  IF (_id IS NULL)
  THEN
    INSERT INTO profile (xp, balance) VALUES (_xp, _balance);
    SET _id = LAST_INSERT_ID();
  END IF;

  INSERT INTO profile (id, xp, balance) VALUES (_id, _xp, _balance)
  ON DUPLICATE KEY UPDATE xp = _xp, balance = _balance;

  IF (_discordid IS NOT NULL)
  THEN
    INSERT INTO profile_discord (id, discordid) VALUES (_id, _discordid)
    ON DUPLICATE KEY UPDATE discordid = _discordid;
  ELSE
    DELETE FROM profile_discord
    WHERE id = _id;
  END IF;

  IF (_discordid IS NULL)
  THEN
    SIGNAL SQLSTATE '45001'
    SET MESSAGE_TEXT = 'No valid ID was assigned!';
  END IF;

  COMMIT;
END;
//

DELIMITER ;


