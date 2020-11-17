--- Sentencias SQL para la creaci?n del esquema de aforocc
--- Las tablas tienen prefijo A_ para facilitar su acceso desde SQL Developer

-- USO
-- Copie el contenido de este archivo en una pesta?a SQL de SQL Developer
-- Ejec?telo como un script - Utilice el bot?n correspondiente de la pesta?a utilizada

-- Creaci?n del secuenciador

create sequence aforocc_sequence;

-- Creaci?n de la tabla VISITANTE y especificaci?n de sus restricciones

CREATE TABLE A_VISITANTE
	(ID NUMBER,
	NOMBRE VARCHAR2(20 BYTE),
	TIPO VARCHAR2(20 BYTE),
	TELEFONO NUMBER,
	CORREO VARCHAR2(20 BYTE),
	NOMCONTACTO VARCHAR2(20 BYTE),
	NUMCONTACTO NUMBER,
	ESTADO VARCHAR2(20 BYTE),
	TEMPERATURA NUMBER,
	CONSTRAINT A_VISITANTE_PK PRIMARY KEY (ID));

ALTER TABLE A_VISITANTE
    ADD CONSTRAINT CK_VISITANTE_EST 
    CHECK (ESTADO IN ('Positivo', 'Rojo', 'Naranja', 'Verde'))
    
ENABLE;

ALTER TABLE A_VISITANTE
    ADD CONSTRAINT CK_VISITANTE_TIPO
    CHECK (TIPO IN ('Empleado', 'Cliente', 'Domiciliario'))
    
ENABLE;

-- Creaci?n de la tabla CARNET y especificaci?n de sus restricciones

CREATE TABLE A_CARNET
	(ID NUMBER,
	IDVISITANTE NUMBER,
	CONSTRAINT A_CARNET_PK PRIMARY KEY (ID));

ALTER TABLE A_CARNET
ADD CONSTRAINT fk_a_visitante
    FOREIGN KEY (idvisitante)
    REFERENCES a_visitante(id)
ENABLE;

-- Creaci?n de la tabla HORARIO y especificaci?n de sus restricciones

CREATE TABLE A_HORARIO
   (ID NUMBER,
    HORAENTRADA NUMBER,	
    HORASALIDA NUMBER,	
    CONSTRAINT A_HORARIO_PK PRIMARY KEY (ID));

-- Creaci?n de la tabla CENTROCOMERCIAL y especificaci?n de sus restricciones

CREATE TABLE A_CENTROCOMERCIAL
   (ID NUMBER, 
    NOMBRE VARCHAR2(255 BYTE), 
    AFOROMAX NUMBER,	
    IDHORARIO NUMBER,
    CONSTRAINT A_CENTROCOMERCIAL_PK PRIMARY KEY (ID));
    
ALTER TABLE A_CENTROCOMERCIAL
    ADD CONSTRAINT UN_CCOMERCIAL_NOMBRE 
    UNIQUE (NOMBRE)
ENABLE;

ALTER TABLE A_CENTROCOMERCIAL
ADD CONSTRAINT fk_a_horario
    FOREIGN KEY (idhorario)
    REFERENCES a_horario(id)
ENABLE;

-- Creaci?n de la tabla LECTOR y especificaci?n de sus restricciones

CREATE TABLE A_LECTOR
   (ID NUMBER,
    UBICACION VARCHAR2(20 BYTE),
    CONSTRAINT A_LECTOR_PK PRIMARY KEY (ID));

-- Creaci?n de la tabla LECTORCC y especificaci?n de sus restricciones

CREATE TABLE A_LECTORCC
   (IDLECTOR NUMBER,
    IDCC NUMBER,
    CONSTRAINT A_LECTORCC_PK PRIMARY KEY (IDLECTOR,IDCC));

ALTER TABLE A_LECTORCC
ADD CONSTRAINT fk_a_lector
    FOREIGN KEY (idlector)
    REFERENCES a_lector(id)
ENABLE;

ALTER TABLE A_LECTORCC
ADD CONSTRAINT fk_a_centrocomercial
    FOREIGN KEY (idcc)
    REFERENCES a_centrocomercial(id)
ENABLE;

-- Creaci?n de la tabla ESPACIO y especificaci?n de sus restricciones

CREATE TABLE A_ESPACIO
	(ID NUMBER,
	IDCC NUMBER,
	NOMBRE VARCHAR2(20 BYTE),
	AREA NUMBER,
	TIPO VARCHAR2(20 BYTE),
    ESTADO VARCHAR2(20 BYTE),
	
	CONSTRAINT A_ESPACIO_PK PRIMARY KEY (ID));

ALTER TABLE A_ESPACIO
ADD CONSTRAINT fk_b_centrocomercial
    FOREIGN KEY (idcc)
    REFERENCES a_centrocomercial(id)
ENABLE;

ALTER TABLE A_ESPACIO
    ADD CONSTRAINT CK_ESPACIO_EST 
    CHECK (ESTADO IN ('Desocupado', 'Deshabilitado', 'Rojo','Naranja', 'Verde'))
ENABLE;

-- Creaci?n de la tabla ESTABLECIMIENTO y especificaci?n de sus restricciones

CREATE TABLE A_ESTABLECIMIENTO
	(ID NUMBER,
	IDESPACIO NUMBER,
	IDHORARIO NUMBER,
	NOMBRE VARCHAR2(20 BYTE),
	TIPO VARCHAR2(20 BYTE),
	AFOROMAX NUMBER,
	CONSTRAINT A_ESTABLECIMIENTO_PK PRIMARY KEY (ID));

ALTER TABLE A_ESTABLECIMIENTO
ADD CONSTRAINT fk_a_espacio
    FOREIGN KEY (idespacio)
    REFERENCES a_espacio(id)
ENABLE;

ALTER TABLE A_ESTABLECIMIENTO
ADD CONSTRAINT fk_b_horario
    FOREIGN KEY (idhorario)
    REFERENCES a_horario(id)
ENABLE;


-- Creaci?n de la tabla LECTORESPACIO y especificaci?n de sus restricciones

CREATE TABLE A_LECTORESPACIO
   (IDLECTOR NUMBER,
    IDLOCAL NUMBER,
    CONSTRAINT A_LECTORESPACIO_PK PRIMARY KEY (IDLECTOR,IDLOCAL));

ALTER TABLE A_LECTORESPACIO
ADD CONSTRAINT fk_b_lector
    FOREIGN KEY (idlector)
    REFERENCES a_lector(id)
ENABLE;

ALTER TABLE A_LECTORESPACIO
ADD CONSTRAINT fk_a_establecimiento
    FOREIGN KEY (idlocal)
    REFERENCES a_establecimiento(id)
ENABLE;

-- Creaci?n de la tabla VISITA y especificaci?n de sus restricciones

CREATE TABLE A_VISITA
   (IDVISITANTE NUMBER,
    IDLECTOR NUMBER,
    FECHAENTRADA VARCHAR2(20 BYTE),
    FECHASALIDA VARCHAR2(20 BYTE),    
    CONSTRAINT A_VISITA_PK PRIMARY KEY (IDVISITANTE,IDLECTOR));

ALTER TABLE A_VISITA
ADD CONSTRAINT fk_b_visitante
    FOREIGN KEY (idvisitante)
    REFERENCES a_visitante(id)
ENABLE;

ALTER TABLE A_VISITA
ADD CONSTRAINT fk_c_lector
    FOREIGN KEY (idlector)
    REFERENCES a_lector (id)
ENABLE;

COMMIT;





