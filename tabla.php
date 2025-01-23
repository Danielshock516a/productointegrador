<?php
// Configuración de la base de datos
$servername = "localhost";
$username = "root"; // Cambiar por el usuario de tu base de datos
$password = ""; // Cambiar por la contraseña de tu base de datos
$dbname = "usuariosdb";

// Crear conexión
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar conexión
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Verificar si se recibieron datos
if (isset($_POST['Nombre']) && isset($_POST['Registro']) && isset($_POST['Calificacion'])) {
    $nombre = $_POST['Nombre'];
    $registro = $_POST['Registro'];
    $calificacion = $_POST['Calificacion'];

    // Insertar datos en la tabla
    $sql = "INSERT INTO calificaciones (Nombre, Registro, Calificacion) VALUES ('$nombre', '$registro', '$calificacion')";

    if ($conn->query($sql) === TRUE) {
        echo "Datos guardados correctamente";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
} else {
    echo "Faltan datos";
}

// Cerrar conexión
$conn->close();
?>
