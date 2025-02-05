<?php
include "./myPDO.php";

if (!isset($_REQUEST["email"]) || !isset($_REQUEST["password"])) {
    die("Error");
}

$email = $_REQUEST["email"];
$password = $_REQUEST["password"];

$conn = myPDO::getInstance();
$query = "INSERT INTO `users`(`Email`, `Password`) VALUES (:email, :password)";
$stmm = $conn -> prepare($query);
$stmm -> bindParam(":email", $email);
$stmm -> bindParam(":password", $password);
$stmm -> execute();

$response = $stmm -> rowCount();

$json = array();

if ($response == 1) {
    $json["message"] = "okey";
    echo json_encode($json);
    exit;
}
else {
    $json["message"] = "Failed";
    echo json_encode($json);
    exit;
}