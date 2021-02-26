CREATE TABLE alter_table (
	id INT,
	name VARCHAR(50),
	fKey INT
);
CREATE INDEX myIndex ON alter_table (fKey);

CREATE TABLE table_fk (
	id INT Primary key
);
CREATE INDEX myIndex2 ON table_fk (id);