  SELECT TABLE_NAME PHYSICAL_NAME
        ,TABLE_COMMENT LOGICAL_NAME
    FROM INFORMATION_SCHEMA.TABLES
   WHERE TABLE_SCHEMA <> 'information_schema'
ORDER BY TABLE_NAME ASC