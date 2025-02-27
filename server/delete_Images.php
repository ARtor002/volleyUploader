<?php

include "./myPDO.php";

if (!isset($_REQUEST["ids"])) {
    die("error");
}

$ids = $_REQUEST["ids"];

$conn = myPDO::getInstance();
$query = "DELETE FROM `images` WHERE Id IN ($ids)";
$stmm = $conn -> prepare($query);
$stmm ->execute();

$res = $stmm -> rowCount();
$json = array();

if($res == 0){
    $json["message"] = "delete_failed";
    echo json_encode($json);
    exit;
}else{
    $json["message"] = "delete_okey";
    echo json_encode($json);
    exit;
}