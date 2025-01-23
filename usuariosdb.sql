-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-01-2025 a las 14:22:40
-- Versión del servidor: 10.1.21-MariaDB
-- Versión de PHP: 7.0.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `usuariosdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `calificaciones`
--

CREATE TABLE `calificaciones` (
  `Nombre` varchar(20) NOT NULL,
  `Registro` int(11) NOT NULL,
  `calificacion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `calificaciones`
--

INSERT INTO `calificaciones` (`Nombre`, `Registro`, `calificacion`) VALUES
('Ana López', 1001, 86),
('Carlos Pérez', 1002, 90),
('María García', 1003, 78),
('Jorge Hernández', 1004, 89),
('Sofía Torres', 1005, 95),
('Luis Martínez', 1006, 70),
('Isabel Rojas', 1007, 83),
('Pedro Jiménez', 1008, 65),
('Fernanda Castillo', 1009, 93),
('Andrés Chávez', 1010, 75);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `nombre` varchar(50) NOT NULL,
  `contrasena` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`nombre`, `contrasena`) VALUES
('JuanPerez', '1'),
('MariaLopez', 'password'),
('CarlosGomez', 'abc123'),
('AnaMartinez', 'qwerty'),
('PedroHernandez', '111111'),
('LuisaDiaz', '123abc'),
('JoseGarcia', 'simple'),
('SofiaMendoza', 'letmein'),
('RamonRuiz', '654321'),
('CarlaFernandez', 'mypassword'),
('Juan', '1234'),
('Juanin', '1');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
