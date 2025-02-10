<?php
    include './myPDO.php';
    
    if (!isset($_REQUEST["email"])){
        die("error");
    }
    
    $email = $_REQUEST["email"];
    $conn = myPDO::getInstance();
    $query = "SELECT image FROM images WHERE email = :email";
    $stmm = $conn->prepare();
    $stmm->bindparam(":email", $email);
    $stmm->execute();
    
    while ($res = $stmm->fetch(PDO::FETCH_ASSOC)) {
        
    
    