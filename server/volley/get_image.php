<?php
include "./myPDO.php";

if (!isset($_REQUEST["email"])) {
    die("error");
}

$email = $_REQUEST["email"];

$conn = myPDO::getInstance();
$query = "SELECT * FROM images WHERE Email = :email";
$stmm = $conn ->prepare($query);
$stmm->bindParam(":email", $email);
$stmm->execute();

$json = array();

while ($res = $stmm->fetch(PDO::FETCH_ASSOC)) {
    $tmp = array();
    $tmp["id"] = $res["Id"];
    $tmp["email"] = $res["Email"];
    $tmp["image"] = $res["Image"];
    $tmp["uploaded"] = $res["Uploaded_At"];

    array_push($json, $tmp);
}

echo json_encode($json);