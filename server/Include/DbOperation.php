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

    public function registrarPonto($tag)
    {
        $stmt = $this->conn->prepare("INSERT INTO registros (tag) VALUES (?);");
        $stmt->bind_param('s', $tag);
        $retorno = $stmt->execute();
        $stmt->close();
        return $retorno;
    }

    public function getRegistros()
    {
        $stmt = $this->conn->prepare(
            "SELECT
              r.id_registro,
              r.data_hora,
              u.tag,
              u.nome,
              u.foto
            FROM registros r 
            JOIN usuarios u 
            WHERE r.tag = u.tag ORDER BY r.data_hora DESC");
        $stmt->execute();
        $registros = $stmt->get_result();
        $stmt->close();
        return $registros;
    }
}