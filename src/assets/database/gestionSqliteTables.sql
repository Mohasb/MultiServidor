BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "clientes" (
	"dni"	TEXT,
	"nombre"	TEXT,
	"apellido"	TEXT,
	"fechaNacimiento"	TEXT,
	PRIMARY KEY("dni")
);
CREATE TABLE IF NOT EXISTS "facturas" (
	"concepto"	TEXT,
	"importe"	REAL,
	"fechaFactura"	TEXT,
	"dni_cliente"	TEXT
);
COMMIT;
