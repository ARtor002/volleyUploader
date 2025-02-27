<?php
    include './myPDO.php';
    
    if (!isset($_REQUEST["email"])){
        die("error");
    }
    
    $email = $_REQUEST["email"];
    $conn = myPDO::getInstance();
    $query = "SELECT image FROM images WHERE email = :email";
    $stmm = $conn->prepare($query);
    $stmm->bindparam(":email", $email);
    $stmm->execute();
    
    while ($res = $stmm->fetch(PDO::FETCH_ASSOC)) {
        unlink($res["image"]);  
    }
    
//    $conn = myPDO::getInstance();
    $query = "DELETE FROM `images` WHERE email = :email";
    $stmm = $conn->prepare($query);
    $stmm->bindparam(":email", $email);
    $stmm->execute();
    
    $query = "DELETE FROM `users` WHERE email = :email";
    $stmm = $conn->prepare($query);
    $stmm->bindparam(":email", $email);
    $stmm->execute();
    
    $res = $stmm -> rowcount();
    $json = array();
    
    if ($res != 0){
        $json["message"] = "Delete_okey";
        echo json_encode($json);
    } else {
        $json["message"] = "Delete_err";
        echo json_encode($json);
    }