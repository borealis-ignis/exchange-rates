DROP SCHEMA IF EXISTS `erates_db`;
CREATE SCHEMA IF NOT EXISTS `erates_db` DEFAULT CHARACTER SET utf8;
USE `erates_db`;

-- -----------------------------------------------------
-- Table `erates_db`.`Banks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `erates_db`.`Banks`;

CREATE TABLE IF NOT EXISTS `erates_db`.`Banks` (
  `Id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Code` VARCHAR(20) NOT NULL,
  `Active` TINYINT(1) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Code_UNIQUE` (`Code`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erates_db`.`Currencies`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `erates_db`.`Currencies`;

CREATE TABLE IF NOT EXISTS `erates_db`.`Currencies` (
  `Id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Code` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE INDEX `Code_UNIQUE` (`Code`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erates_db`.`ExchangeRates`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `erates_db`.`ExchangeRates`;

CREATE TABLE IF NOT EXISTS `erates_db`.`ExchangeRates` (
  `Id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `BuyRate` DECIMAL(10,4) NOT NULL,
  `SellRate` DECIMAL(10,4) NOT NULL,
  `CurrencyId` INT UNSIGNED NOT NULL,
  `BankId` INT UNSIGNED NOT NULL,
  `UpdateDate` DATETIME NOT NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `fk_ExchangeRates_Currency`
    FOREIGN KEY (`CurrencyId`)
    REFERENCES `erates_db`.`Currencies` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ExchangeRates_Bank`
    FOREIGN KEY (`BankId`)
    REFERENCES `erates_db`.`Banks` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;