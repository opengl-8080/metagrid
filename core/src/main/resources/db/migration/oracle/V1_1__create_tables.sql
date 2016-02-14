-- メタテーブル定義
CREATE TABLE META_TABLE_DEFINITION (
  ID            NUMBER(10)          NOT NULL,
  PHYSICAL_NAME VARCHAR2(128 CHAR)  NOT NULL,
  LOGICAL_NAME  VARCHAR2(256 CHAR)  NOT NULL,
  CONSTRAINT TABLE_DEFINITION_PK PRIMARY KEY (ID),
  CONSTRAINT TABLE_DEFINITION_UK1 UNIQUE (PHYSICAL_NAME)
);

-- メタ―テーブル定義 シーケンス
CREATE SEQUENCE META_TABLE_DEFINITION_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
;

-- アップロードファイル
CREATE TABLE UPLOAD_FILE (
    ID                      NUMBER(10)          NOT NULL,
    FILE_NAME               VARCHAR2(256 CHAR)  NOT NULL,
    CONTENTS                CLOB                NULL,
    TOTAL_RECORD_COUNT      NUMBER(7)           NOT NULL,
    PROCESSED_RECORD_COUNT  NUMBER(7)           NOT NULL,
    TOTAL_PASSED_TIME       NUMBER(10)          NOT NULL,
    STATUS                  VARCHAR2(12 CHAR)  NOT NULL,
    CONSTRAINT UPLOAD_FILE_PK PRIMARY KEY (ID)
);

-- アップロードファイル シーケンス
CREATE SEQUENCE UPLOAD_FILE_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
;

-- エラーレコード
CREATE TABLE ERROR_RECORD (
    ID              NUMBER(10)  NOT NULL,
    UPLOAD_FILE_ID  NUMBER(10)  NOT NULL,
    CONTENTS        CLOB        NOT NULL,
    ORDER_NO        NUMBER(7)           NOT NULL,
    CONSTRAINT ERROR_RECORD_PK PRIMARY KEY (ID),
    CONSTRAINT ERROR_RECORD_FK1 FOREIGN KEY (UPLOAD_FILE_ID) REFERENCES UPLOAD_FILE (ID)
);

-- エラーレコード シーケンス
CREATE SEQUENCE ERROR_RECORD_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
;

-- エラーメッセージ
CREATE TABLE ERROR_RECORD_MESSAGE (
    ID              NUMBER(10)          NOT NULL,
    ERROR_RECORD_ID NUMBER(10)          NOT NULL,
    FIELD_NAME      VARCHAR2(128 CHAR)  NULL,
    MESSAGE         VARCHAR2(256 CHAR)  NULL,
    ORDER_NO        NUMBER(7)           NOT NULL,
    CONSTRAINT ERROR_RECORD_MESSAGE_PK PRIMARY KEY (ID),
    CONSTRAINT ERROR_RECORD_MESSAGE_FK1 FOREIGN KEY (ERROR_RECORD_ID) REFERENCES ERROR_RECORD (ID)
);

-- エラーメッセージ シーケンス
CREATE SEQUENCE ERROR_RECORD_MESSAGE_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
;
