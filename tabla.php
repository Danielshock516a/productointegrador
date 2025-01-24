<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

// Detalles de la conexión a la base de datos
$host = "localhost";
$dbname = "usuariosdb"; 
$username = "root";
$password = "";

// Leer el cuerpo de la solicitud POST (asumimos que el cuerpo contiene el JSON)
$data = file_get_contents("php://input");

// Decodificar el JSON en un array asociativo
$datos = json_decode($data, true);

// Verificar si los datos fueron decodificados correctamente
if ($datos === null) {
    echo "Error: El JSON recibido es inválido.";
    exit;
}

// Extraer los valores del JSON con validación
$nombre = isset($datos['Nombre']);  // Aseguramos que 'Nombre' esté presente
$registro = isset($datos['Registro']) ? (int)$datos['Registro'] : 0; // Convertir a int
$calificacion = isset($datos['Calificacion']) ? (int)$datos['Calificacion'] : 0; // Convertir a int

// Verificar que los datos esenciales no estén vacíos
if (empty($nombre) || $registro == 0 || $calificacion == 0) {
    echo "Error: Los datos no son válidos o incompletos.";
    exit;
}

// Conexión a la base de datos (asegúrate de que la conexión esté abierta en $conn)
include('conexion.php');  // Asegúrate de incluir la conexión a la base de datos

// Preparar la consulta para evitar inyección SQL
$sql = $conn->prepare("INSERT INTO calificaciones (nombre, registro, calificacion) VALUES (?, ?, ?)");
$sql->bind_param("ssi", $nombre, $registro, $calificacion);  // 's' para string, 'i' para int

// Ejecutar la consulta
if ($sql->execute()) {
    echo "Datos insertados correctamente";
} else {
    echo "Error al insertar datos: " . $sql->error;
}

// Cerrar la consulta y la conexión
$sql->close();
$conn->close();
?>
