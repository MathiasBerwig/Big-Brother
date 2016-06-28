<?php

/**
 * Created by PhpStorm.
 * User: mathias.berwig
 * Date: 28/06/2016
 * Time: 10:01
 */
class DbOperation
{

    private $conn;

    /**
     * DbOperation constructor.
     */
    public function __construct()
    {
        require_once 'DbConnect.php';
        $db = new DbConnection();
        $this->conn = $db->connect();
    }
}