CREATE TABLE alter_table (
	id INT,
	name VARCHAR(50),
	fKey INT,
	Index fKeyIndex (fKey)
);

CREATE TABLE table_fk (
	id INT,
	Index idIndex (id)
);