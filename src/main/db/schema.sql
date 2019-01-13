
-- -----------------------------------------------------
-- Schema netflix
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `netflix` ;
USE `netflix` ;

-- -----------------------------------------------------
-- Table `netflix`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`customer` ;

CREATE TABLE IF NOT EXISTS `netflix`.`customer` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Auto Generated customer id',
  `country_code` CHAR(2) NOT NULL COMMENT 'ISO 3166 country code (alpha 2)',
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `active` TINYINT(1) NOT NULL DEFAULT TRUE,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Table storing customer’s payment method ';


-- -----------------------------------------------------
-- Table `netflix`.`Payment_Method`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`Payment_Method` ;

CREATE TABLE IF NOT EXISTS `netflix`.`Payment_Method` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `payment_type` VARCHAR(45) NULL,
  `payment_token` VARCHAR(45) NULL,
  `payment_gateway` VARCHAR(45) NULL,
  `customer_id` INT  NOT NULL,
  PRIMARY KEY (`ID`, `customer_id`),
  INDEX `fk_Payment_Method_customer_idx` (`customer_id` ASC),
  CONSTRAINT `fk_Payment_Method_customer`
    FOREIGN KEY (`customer_id`)
    REFERENCES `netflix`.`customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Table storing customers payment method. Payment method could be a CC, bank details , PayPal , gift card , etc.';


-- -----------------------------------------------------
-- Table `netflix`.`entitlement`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`entitlement` ;

CREATE TABLE IF NOT EXISTS `netflix`.`entitlement` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
COMMENT = 'Entitlements are granular features of the system.';


-- -----------------------------------------------------
-- Table `netflix`.`entitlement_set`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`entitlement_set` ;

CREATE TABLE IF NOT EXISTS `netflix`.`entitlement_set` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(200) NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = 'Entitlement set is a collection of features/entitlements';


-- -----------------------------------------------------
-- Table `netflix`.`entitlement_set_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`entitlement_set_mapping` ;

CREATE TABLE IF NOT EXISTS `netflix`.`entitlement_set_mapping` (
  `entitlement_set_id` INT NOT NULL,
  `entitlement_ID` INT NOT NULL,
  PRIMARY KEY (`entitlement_set_id`, `entitlement_ID`),
  INDEX `fk_entitlement_set_has_Entitlement_Entitlement1_idx` (`entitlement_ID` ASC),
  INDEX `fk_entitlement_set_has_Entitlement_entitlement_set1_idx` (`entitlement_set_id` ASC),
  CONSTRAINT `fk_entitlement_set_has_Entitlement_entitlement_set1`
    FOREIGN KEY (`entitlement_set_id`)
    REFERENCES `netflix`.`entitlement_set` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_entitlement_set_has_Entitlement_Entitlement1`
    FOREIGN KEY (`entitlement_ID`)
    REFERENCES `netflix`.`entitlement` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `netflix`.`customer_grant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`customer_grant` ;

CREATE TABLE IF NOT EXISTS `netflix`.`customer_grant` (
  `customer_id` INT NOT NULL,
  `entitlement_set_id` INT NOT NULL,
  `expiry` DATETIME NULL,
  PRIMARY KEY (`customer_id`, `entitlement_set_id`),
  INDEX `fk_customer_has_entitlement_set_entitlement_set1_idx` (`entitlement_set_id` ASC),
  INDEX `fk_customer_has_entitlement_set_customer1_idx` (`customer_id` ASC),
  CONSTRAINT `fk_customer_has_entitlement_set_customer1`
    FOREIGN KEY (`customer_id`)
    REFERENCES `netflix`.`customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_customer_has_entitlement_set_entitlement_set1`
    FOREIGN KEY (`entitlement_set_id`)
    REFERENCES `netflix`.`entitlement_set` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'This table holds the entitlement sets that are granted to customers. The grant could have an expiry date.';


-- -----------------------------------------------------
-- Table `netflix`.`product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`product` ;

CREATE TABLE IF NOT EXISTS `netflix`.`product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku` VARCHAR(45) NOT NULL,
  `status` VARCHAR(15) NULL,
  `entitlement_set_id` INT NOT NULL,
  PRIMARY KEY (`id`, `entitlement_set_id`),
  INDEX `fk_product_entitlement_set1_idx` (`entitlement_set_id` ASC),
  CONSTRAINT `fk_product_entitlement_set1`
    FOREIGN KEY (`entitlement_set_id`)
    REFERENCES `netflix`.`entitlement_set` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Products in the system';


-- -----------------------------------------------------
-- Table `netflix`.`price`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`price` ;

CREATE TABLE IF NOT EXISTS `netflix`.`price` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country_code` CHAR(3) NOT NULL,
  `currency_code` CHAR(3) NOT NULL,
  `effective_date` DATETIME NOT NULL,
  `status` VARCHAR(15) NULL,
  `product_id` INT NOT NULL,
  `amount` FLOAT NOT NULL,
  INDEX `index1` (`country_code` ASC),
  INDEX `effective_date` (`effective_date` ASC),
  INDEX `status` (`status` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_price_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `netflix`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `netflix`.`subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`subscription` ;

CREATE TABLE IF NOT EXISTS `netflix`.`subscription` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` CHAR(10) NOT NULL,
  `creation_date` DATETIME NULL,
  `first_bill_date` DATETIME NULL,
  `update_date` DATETIME NULL,
  `customer_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`id`, `customer_id`, `product_id`),
  INDEX `fk_subscription_customer1_idx` (`customer_id` ASC),
  INDEX `fk_subscription_product1_idx` (`product_id` ASC),
  CONSTRAINT `fk_subscription_customer1`
    FOREIGN KEY (`customer_id`)
    REFERENCES `netflix`.`customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_subscription_product1`
    FOREIGN KEY (`product_id`)
    REFERENCES `netflix`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This table holds the subscription information for customers.';


-- -----------------------------------------------------
-- Table `netflix`.`payment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`payment` ;

CREATE TABLE IF NOT EXISTS `netflix`.`payment` (
  `id` INT NOT NULL,
  `payment_method` INT NULL,
  `invoice_id` INT NULL,
  `gateway` VARCHAR(45) NULL,
  `amount` FLOAT NULL,
  `currency` CHAR(3) NULL,
  `status` VARCHAR(10) NULL,
  `error` VARCHAR(45) NULL,
  `Payment_Method_ID` INT NOT NULL,
  PRIMARY KEY (`id`, `Payment_Method_ID`),
  INDEX `fk_payment_Payment_Method1_idx` (`Payment_Method_ID` ASC),
  CONSTRAINT `fk_payment_Payment_Method1`
    FOREIGN KEY (`Payment_Method_ID`)
    REFERENCES `netflix`.`Payment_Method` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This table holds the payment information.';


-- -----------------------------------------------------
-- Table `netflix`.`Invoice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`Invoice` ;

CREATE TABLE IF NOT EXISTS `netflix`.`Invoice` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `due_date` DATETIME NULL,
  `created_date` DATETIME NULL,
  `service_period_start` DATETIME NULL,
  `service_period_end` DATETIME NULL,
  `subscription_id` INT NOT NULL,
  `payment_id` INT NOT NULL,
  `balance` FLOAT NULL,
  PRIMARY KEY (`id`, `subscription_id`, `payment_id`),
  INDEX `fk_Invoice_subscription1_idx` (`subscription_id` ASC),
  INDEX `fk_Invoice_payment1_idx` (`payment_id` ASC),
  CONSTRAINT `fk_Invoice_subscription1`
    FOREIGN KEY (`subscription_id`)
    REFERENCES `netflix`.`subscription` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Invoice_payment1`
    FOREIGN KEY (`payment_id`)
    REFERENCES `netflix`.`payment` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This table holds the monthly invoices for customers';


-- -----------------------------------------------------
-- Table `netflix`.`invoice_line_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`invoice_line_item` ;

CREATE TABLE IF NOT EXISTS `netflix`.`invoice_line_item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NULL,
  `currency` VARCHAR(45) NULL,
  `amount` FLOAT NULL,
  `type` VARCHAR(10) NULL,
  `Invoice_id` INT NOT NULL,
  PRIMARY KEY (`id`, `Invoice_id`),
  INDEX `fk_invoice_line_item_Invoice1_idx` (`Invoice_id` ASC),
  CONSTRAINT `fk_invoice_line_item_Invoice1`
    FOREIGN KEY (`Invoice_id`)
    REFERENCES `netflix`.`Invoice` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'The line items of an invoice';


-- -----------------------------------------------------
-- Table `netflix`.`Ammendment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `netflix`.`Ammendment` ;

CREATE TABLE IF NOT EXISTS `netflix`.`Ammendment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(15) NULL,
  `effective_date` DATETIME NULL,
  `created_date` DATETIME NULL,
  `subscription_id` INT NOT NULL,
  PRIMARY KEY (`id`,`subscription_id`),
  CONSTRAINT `fk_Ammendment_subscription1`
    FOREIGN KEY (`subscription_id`)
    REFERENCES `netflix`.`subscription` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `netflix`.`customer`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`customer` (`id`, `country_code`, `first_name`, `last_name`, `email`, `active`, `create_date`, `last_update`) VALUES (DEFAULT, 'US', 'John', 'Doe', 'jdoe@gmail.com', active, '2018-09-01 00:00:00', NULL);
INSERT INTO `netflix`.`customer` (`id`, `country_code`, `first_name`, `last_name`, `email`, `active`, `create_date`, `last_update`) VALUES (DEFAULT, 'GB', 'Shah', 'Bagdadi', 'email.shanu@gmail.com', active, '2018-05-02 08:32:34', NULL);
INSERT INTO `netflix`.`customer` (`id`, `country_code`, `first_name`, `last_name`, `email`, `active`, `create_date`, `last_update`) VALUES (DEFAULT, 'JP', 'Haru', 'Matsu', 'haru@gmail.com', active, '2018-08-12 07:23:34', NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `netflix`.`entitlement`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_1', '1 device Stream ');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_2', '2 device streams');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_3', '3 device streams');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_HD_1', '1 HD STREAM');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_HD_2', '2 HD STREAM');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'STREAM_UHD_1', '1 Ultra high definition stream');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'DVD_1_MONTH_2', '1 DVD out, 2 per month rental');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'DVD_1_MONTH_UNLIMITED', '1 DVD out, unlimited per month');
INSERT INTO `netflix`.`entitlement` (`ID`, `name`, `description`) VALUES (DEFAULT, 'DVD_2_MONTH_UNLIMITED', '2 DVD out , unlimited per month');

COMMIT;


-- -----------------------------------------------------
-- Data for table `netflix`.`entitlement_set`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Basic online', 'Basic');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Standard Online', 'Standard');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Premium Online', 'Premium');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Ultra Onlline', 'Ultra');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Basic DVD plan', 'DVD Basic');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Standard DVD plan', 'DVD Standard');
INSERT INTO `netflix`.`entitlement_set` (`id`, `description`, `name`) VALUES (DEFAULT, 'Premium DVD plan', 'DVD Premium');

COMMIT;


-- -----------------------------------------------------
-- Data for table `netflix`.`entitlement_set_mapping`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (1, 1);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (2, 2);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (3, 3);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (2, 4);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (3, 5);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (3, 6);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (5, 7);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (6, 8);
INSERT INTO `netflix`.`entitlement_set_mapping` (`entitlement_set_id`, `entitlement_ID`) VALUES (7, 9);

COMMIT;



-- -----------------------------------------------------
-- Data for table `netflix`.`product`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-basic-1', 'active', 1);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-std-1', 'active', 2);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-premium-1', 'active', 3);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-ultra-1', 'active', 4);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-basic-dvd', 'active', 5);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-std-dvd', 'active', 6);
INSERT INTO `netflix`.`product` (`id`, `sku`, `status`, `entitlement_set_id`) VALUES (DEFAULT, 'n-premium-dvd', 'active', 7);

COMMIT;



-- -----------------------------------------------------
-- Data for table `netflix`.`price`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'US', 'USD', '2015-01-05', 'active', 1, 7.99);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'IN', 'INR', '2015-08-15', 'active', 1, 700.00);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'GB', 'GBP', '2015-08-15', 'active', 1, 7.00);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'JP', 'JPY', '2016-08-10', 'active', 1, 973.00);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'US', 'USD', '2015-05-17', 'active', 2, 10.99);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'GB', 'GBP', '2016-02-25 10:00:00', 'active', 2, 10.99);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'JP', 'JPY', '2017-11-10 05:00:00', 'active', 2, 1200.00);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'US', 'USD', '2016-06-10 01:00:00', 'active', 3, 13.99);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'GB', 'GBP', '2016-08-01 05:30:10', 'active', 3, 12.50);
INSERT INTO `netflix`.`price` (`id`, `country_code`, `currency_code`, `effective_date`, `status`, `product_id`, `amount`) VALUES (DEFAULT, 'JP', 'JPY', '2017-09-01 06:00:00', 'active', 3, 1600.00);

COMMIT;

-- -----------------------------------------------------
-- Data for table `netflix`.`subscription`
-- -----------------------------------------------------
START TRANSACTION;
USE `netflix`;
INSERT INTO `netflix`.`subscription` (`id`, `status`, `creation_date`, `first_bill_date`, `update_date`, `customer_id`, `product_id`) VALUES (DEFAULT, 'active', '2018-10-01 10:01:01', '2018-11-02', NULL, 1, 1);
INSERT INTO `netflix`.`subscription` (`id`, `status`, `creation_date`, `first_bill_date`, `update_date`, `customer_id`, `product_id`) VALUES (DEFAULT, 'active', '2018-10-12 04:23:25', '2018-11-12', NULL, 2, 2);
INSERT INTO `netflix`.`subscription` (`id`, `status`, `creation_date`, `first_bill_date`, `update_date`, `customer_id`, `product_id`) VALUES (DEFAULT, 'active', '2018-11-01 09:12:12', '2018-12-01', NULL, 3, 3);

COMMIT;

