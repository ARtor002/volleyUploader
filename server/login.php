<?php
include "./myPDO.php";

if (!isset($_REQUEST["email"]) || !isset($_REQUEST["password"])) {
    die("error");
}

$email = $_REQUEST["email"];
$password = $_REQUEST["password"];

$conn = myPDO::getInstance();
$query = "SELECT * FROM users WHERE email = :email AND password = :password";
$stmm = $conn -> prepare($query);
$stmm -> bindParam(":email", $email);
$stmm -> bindParam(":password", $password);

$stmm -> execute();

$response = $stmm -> rowCount();

$json = array();

if ($response == 1) {
    $json["message"] = "Login Successful";
    echo json_encode($json);
    exit;
}
else {
    $json["message"] = "Login Failed";
    echo json_encode($json);
    exit;
}

