CREATE TABLE alter_table (
	id INT,
	name VARCHAR(50),
	fKey INT
	--Index myIndex (fKey)
);
CREATE INDEX myIndex ON alter_table (fKey);

CREATE TABLE table_fk (
	id INT
	-- Index myIndex2 (id)
);
