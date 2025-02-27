<?php
include "./myPDO.php";

if (!isset($_REQUEST["email"]) || !isset($_REQUEST["code"])) {
    die("Error");
}

$email = $_REQUEST["email"];
$code = $_REQUEST["code"];

$conn = myPDO::getInstance();
$query = "SELECT email FROM users WHERE email = :email";
$stmm = $conn -> prepare($query);
$stmm -> bindParam(":email", $email);

$stmm -> execute();

$res = $stmm -> rowCount();

$json = array();

if ($res == 1) {
    $json["message"] = "email-exist";
    echo json_encode($json);
    exit;
}else{
    $json["message"] = "email-okey";
    $message = "Your code is '$code'";
    mail($email,"Your verification Code",$message);

    echo json_encode($json);
    exit;
}
