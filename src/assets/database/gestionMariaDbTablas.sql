CREATE TABLE IF NOT EXISTS `clientes` (
  `dni` varchar(50) NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellido` varchar(50) DEFAULT NULL,
  `fechaNacimiento` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `facturas` (
  `concepto` varchar(50) NOT NULL,
  `importe` decimal(20,6) DEFAULT 0.000000,
  `fechaFactura` varchar(50) DEFAULT NULL,
  `dni_cliente` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
