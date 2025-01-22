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
if (!isset($data["nombre"]) || !isset($data["contraseña"])) {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
    exit();
}

$nombre = $data["nombre"];
$contraseña = $data["contraseña"];

// Verificar si el usuario existe en la base de datos
$stmt = $pdo->prepare("SELECT * FROM usuarios WHERE nombre = :nombre");
$stmt->bindParam(":nombre", $nombre);
$stmt->execute();

if ($stmt->rowCount() == 1) {
    $user = $stmt->fetch(PDO::FETCH_ASSOC);

    // Verificar la contraseña ingresada contra la almacenada en la base de datos
    if ($contraseña === $user["contraseña"]) { // Contraseñas simples, no se utiliza hash
        echo json_encode(["success" => true, "message" => "Login exitoso", "id" => $user["id"]]);
    } else {
        echo json_encode(["success" => false, "message" => "Contraseña incorrecta"]);
    }
} else {
    // Insertar un nuevo usuario si no existe
    $insertStmt = $pdo->prepare("INSERT INTO usuarios (nombre, contraseña) VALUES (:nombre, :contraseña)");
    $insertStmt->bindParam(":nombre", $nombre);
    $insertStmt->bindParam(":contraseña", $contraseña);

    if ($insertStmt->execute()) {
        echo json_encode(["success" => true, "message" => "Usuario registrado exitosamente"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error al registrar el usuario"]);
    }
}
?>


