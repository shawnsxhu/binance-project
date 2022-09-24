DROP TABLE IF EXISTS candles;

CREATE TABLE `candles` (
  `symbol` varchar(45) DEFAULT NULL,
  `load_id` varchar(45) NOT NULL,
  `open_time` varchar(45) NOT NULL,
  `open` varchar(45) DEFAULT NULL,
  `high` varchar(45) DEFAULT NULL,
  `low` varchar(45) DEFAULT NULL,
  `close` varchar(45) DEFAULT NULL,
  `volume` varchar(45) DEFAULT NULL,
  `close_time` varchar(45) NOT NULL,
  `quote_asset_volume` varchar(45) DEFAULT NULL,
  `num_trade` varchar(45) DEFAULT NULL,
  `taker_buy_base_asset_volume` varchar(45) DEFAULT NULL,
  `taker_buy_quote_asset_volume` varchar(45) DEFAULT NULL,
  `_ignore` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`load_id`,`open_time`,`close_time`)
);

DROP TABLE IF EXISTS candle_items;

CREATE TABLE `candle_items` (
  `load_id` varchar(45) NOT NULL,
  `symbol` varchar(45) DEFAULT NULL,
  `open_time` bigint NOT NULL,
  `open` decimal(12,6) DEFAULT NULL,
  `high` decimal(12,6) DEFAULT NULL,
  `low` decimal(12,6) DEFAULT NULL,
  `close` decimal(12,6) DEFAULT NULL,
  `volume` decimal(12,6) DEFAULT NULL,
  `close_time` bigint NOT NULL,
  `quote_asset_volume` decimal(20,10) DEFAULT NULL,
  `num_trade` bigint DEFAULT NULL,
  PRIMARY KEY (`load_id`,`open_time`,`close_time`)
);