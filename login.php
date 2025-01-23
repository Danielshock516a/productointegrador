<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

// Conexión a la base de datos
$host = "localhost";
$dbname = "UsuariosDB";
$username = "root";
$password = "";

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    echo json_encode(["success" => false, "message" => "Error al conectar a la base de datos"]);
    exit();
}

// Obtener los datos enviados
$data = json_decode(file_get_contents("php://input"), true);

// Validar datos
if (!isset($data["nombre"]) || !isset($data["contrasena"])) {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
    exit();
}

$nombre = $data["nombre"];
$contrasena = $data["contrasena"];

// Verificar si el usuario existe en la base de datos
$stmt = $pdo->prepare("SELECT * FROM usuarios WHERE nombre = :nombre");
$stmt->bindParam(":nombre", $nombre);
$stmt->execute();

if ($stmt->rowCount() == 1) {
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($contrasena === $user["contrasena"]) {
        echo json_encode(["success" => true, "message" => "Login exitoso"]);
    } else {
        echo json_encode(["success" => false, "message" => "Contraseña incorrecta"]);
    }
} else {
    $insertStmt = $pdo->prepare("INSERT INTO usuarios (nombre, contrasena) VALUES (:nombre, :contrasena)");
    $insertStmt->bindParam(":nombre", $nombre);
    $insertStmt->bindParam(":contrasena", $contrasena);

    if ($insertStmt->execute()) {
        echo json_encode(["success" => true, "message" => "Usuario registrado exitosamente"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error al registrar el usuario"]);
    }
}

// Asegurarse de no enviar contenido adicional
exit();
?>



