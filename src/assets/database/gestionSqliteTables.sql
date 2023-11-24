BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "clientes" (
	"dni"	TEXT,
	"nombre"	TEXT,
	"apellido"	TEXT,
	"fechaNacimiento"	TEXT,
	PRIMARY KEY("dni")
);

CREATE TABLE IF NOT EXISTS "facturas" (
    "id"       INTEGER NOT NULL,
    "concepto"	TEXT,
	"importe"	REAL,
	"fechaFactura"	TEXT,
	"dni_cliente"	TEXT
	PRIMARY KEY("id"),
    FOREIGN KEY("dni_cliente") REFERENCES "clientes" ("dni")
);
COMMIT;