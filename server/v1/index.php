<?php
/**
 * Created by PhpStorm.
 * User: mathias.berwig
 * Date: 28/06/2016
 * Time: 10:01
 */

include_once '../Libs/Slim/Slim.php';
include_once '../Include/DbOperation.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

function echoResponse($status_code, $response)
{
    // Obtém a instância do Slim
    $app = \Slim\Slim::getInstance();

    // Define o código de resposta HTTP
    $app->status($status_code);

    // Define o tipo de resposta para JSON
    $app->contentType('application/json');

    // Exibe a resposta em formato JSON
    if (sizeof($response) > 0) {
        echo json_encode($response);
    }
}

/**
 * Insere uma linha na tabela REGISTROS com a TAG informada e o horário do servidor de banco de dados.
 * Retorna {@code 201} caso a inserção tenha sido realizada com sucesso, do contrário {@code 400}.
 */
$app->post('/registrarPonto', function () use ($app) {
    $tag = $app->request->post('tag');
    $db = new DbOperation();
    $retorno = $db->registrarPonto($tag);
    if ($retorno) {
        echoResponse(201, null);
    } else {
        echoResponse(400, null);
    }
});

/**
 * Consulta a tabela REGISTROS e retorna todos os registros de ponto em formato de array.
 */
$app->get('/getRegistros', function () use ($app) {
    $retorno = array();
    $db = new DbOperation();
    $result = $db->getRegistros();

    while ($ponto = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["id_registro"] = $ponto["id_registro"];
        $tmp["data_hora"] = $ponto["data_hora"];
        $tmp["tag"] = $ponto["tag"];
        $tmp["nome"] = $ponto["nome"];
        $tmp["foto"] = $ponto["foto"];
        array_push($retorno, $tmp);
    }

    echoResponse(200, $retorno);
});

$app->run();