<?php

include "./config.php";

class myPDO extends PDO {

    private static $instance;

    public function __construct(){
        try{
            parent::__construct("mysql:host=".config::$host.";dbname=".config::$dbname, config::$username,config::$password);
            self::$instance = $this;
            self::$instance->exec("set names utf8");
            self::$instance->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
        
        } catch (Exception $ex) {
            echo "error pdo".$ex->getMessage();
        }
    }    

    public static function getInstance(){
        if (self::$instance == null) {
            self::$instance = new myPDO();
        }
        return self::$instance;
    }

}