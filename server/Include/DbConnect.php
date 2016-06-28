<?php

/**
 * Created by PhpStorm.
 * User: mathias.berwig
 * Date: 28/06/2016
 * Time: 10:01
 */
class DbConnection
{
    private $conn;

    /**
     * DbConnection constructor.
     */
    public function __construct()
    {
    }

    function connect()
    {
        include_once 'Constants.php';
        $this->conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

        if (mysqli_connect_errno()) {
            echo "Falha na conexão com o banco de dados." . mysqli_connect_errno();
        }

        return $this->conn;
    }
}