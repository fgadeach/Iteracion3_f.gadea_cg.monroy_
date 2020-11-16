--- Sentencias SQL para la creaci?n del esquema de aforocc
--- Las tablas tienen prefijo A_ para facilitar su acceso desde SQL Developer

-- USO
-- Copie el contenido deseado de este archivo en una pesta?a SQL de SQL Developer
-- Ejec?telo como un script - Utilice el bot?n correspondiente de la pesta?a utilizada
    
-- Eliminar todas las tablas de la base de datos
DROP TABLE "A_CARNET" CASCADE CONSTRAINTS;
DROP TABLE "A_CENTROCOMERCIAL" CASCADE CONSTRAINTS;
DROP TABLE "A_ESPACIO" CASCADE CONSTRAINTS;
DROP TABLE "A_ESTABLECIMIENTO" CASCADE CONSTRAINTS;
DROP TABLE "A_HORARIO" CASCADE CONSTRAINTS;
DROP TABLE "A_LECTOR" CASCADE CONSTRAINTS;
DROP TABLE "A_LECTORCC" CASCADE CONSTRAINTS;
DROP TABLE "A_LECTORESPACIO" CASCADE CONSTRAINTS;
DROP TABLE "A_VISITA" CASCADE CONSTRAINTS;
DROP TABLE "A_VISITANTE" CASCADE CONSTRAINTS;
COMMMIT;

-- Eliminar el contenido de todas las tablas de la base de datos
-- El orden es importante. Por qu??
delete from a_visita;
delete from a_carnet;
delete from a_lectorcc;
delete from a_lectorespacio;
delete from a_lector;
delete from a_establecimiento;
delete from a_espacio;
delete from a_centrocomercial;
delete from a_visitante;
delete from a_horario;
commit;




