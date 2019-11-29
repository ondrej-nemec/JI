CREATE TABLE Second (
	id INT,
	second_name VARCHAR(20),
	first_id INT
);
SELECT * FROM Second;

--- REVERT ---

DROP TABLE Second;