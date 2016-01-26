CREATE TABLE TABLE_DEFINITION (
  ID INT NOT NULL,
  PHYSICAL_NAME VARCHAR(128) NOT NULL,
  LOGICAL_NAME VARCHAR(256) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE INDEX PHYSICAL_NAME_UK1 (PHYSICAL_NAME ASC)
);
