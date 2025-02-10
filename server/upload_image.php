<?php
include "./myPDO.php";

if (!isset($_REQUEST["image"]) || !isset($_REQUEST["email"])) {
    die("Error");
}

$image = $_REQUEST["image"];
$email = $_REQUEST["email"];

$photosDir = "images/";
$userPhoto = $photosDir . randomChars(70) . ".jpg";

$conn = myPDO::getInstance();
$query = "INSERT INTO `images`(`Email`, `Image`) VALUES (:email, :image)";
$stmm = $conn->prepare($query);
$stmm->bindParam(":image", $userPhoto);
$stmm->bindParam(":email", $email);
$stmm->execute();
    
$response = $stmm->rowCount();

$json = array();

if ($response == 1) {
    file_put_contents($userPhoto, base64_decode($image));
    $json["message"] = "Uploaded";
    echo json_encode($json);
    exit;
}else {
    echo $response;
    $json["message"] = "Failed";
    echo json_encode($json);
    exit;
}

function randomChars($len){
    $chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    
    $randomChars = "";

    for ($i=0; $i < $len; $i++) { 
        $randomChars .= $chars[rand(0, strlen($chars)-1)];
    }
    return $randomChars;
}