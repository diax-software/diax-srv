ALTER TABLE profile
  ADD COLUMN balance BIGINT NOT NULL DEFAULT 0;

DELIMITER //
CREATE PROCEDURE proc_save_profile(
  IN _id BIGINT, _xp BIGINT, _balance BIGINT
) BEGIN
  INSERT INTO profile (id, xp, balance) VALUES (_id, _xp, _balance)
  ON DUPLICATE KEY UPDATE xp = _xp, balance = _balance;
END;
//
DELIMITER ;

