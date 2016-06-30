/*
 * Este é o sketch do Arduino Mega, responsável por receber os dados do RFID
 * do Arduino Uno pela porta serial. 
 *
 * Neste Arduino os pinos 18 e 19 são TX e RX, respectivamente. Eles são
 * conectados nos mesmos pinos 0 e 1 do Arduino Uno (RX -> TX, TX -> RX). 
 * As informações recebidas são enviadas para o web service, que realiza a 
 * inserção das mesmas em um banco de dados relacional.
 *
 * O código é baseado no post "CONTROLE DE ACESSO USANDO LEITOR RFID COM ARDUINO"
 * de ADILSON THOMSEN, disponível em 
 * <http://blog.filipeflop.com/wireless/controle-acesso-leitor-rfid-arduino.html>. 
 */
#include <Ethernet.h>
#include <SPI.h>
#include "RestClient.h"

char tag[9];

#define INTERVALO_LEITURA 600

#define PINO_LED_VERDE 5
#define PINO_LED_VERMELHO 6

// TODO: Definir endereço do servidor
static const char ENDERECO_SERVIDOR[] = "";
static const char RECURSO_POST[] = "/server/v1/registrarPonto";

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

void setup() {
  pinMode(13, OUTPUT);
  
  Serial.println("Iniciando Mega...");
  Serial.begin(9600);
  Serial1.begin(9600); // Serial do Mega para comunicação com Uno
  
  Serial.println("Iniciando rede com DHCP...");
  if (Ethernet.begin(mac) == 0) {
   Serial.println("Falha ao obter endereco de rede.");
  }
  Serial.print("IP: ");
  Serial.println(Ethernet.localIP());
  Serial.print("Mascara: ");
  Serial.println(Ethernet.subnetMask());
  Serial.print("Gateway: ");
  Serial.println(Ethernet.gatewayIP());
  Serial.print("DNS: ");
  Serial.println(Ethernet.dnsServerIP());
  Serial.println();
}

void loop() {
  int i = 0;

  // Verifica se há dados para serem recebidos
  if (Serial1.available()) {
    delay(INTERVALO_LEITURA);
    while (Serial1.available() && i < 9) {
      tag[i++] = Serial1.read();
    }
    tag[i++] = '\0';
  }

  // Verifica se há dados para enviar para o web server
  if(i > 0) {
    postWebservice(tag);
  }
}

void postWebservice(String tag) {
    Serial.println("Enviando a TAG '" + tag + "' para o web service.");
    
    // Cria instância do RestClient
    RestClient client = RestClient(ENDERECO_SERVIDOR);

    // Prepara a TAG para envio
    char aux[15];
    String aux1 = "";
    aux1 = "tag=" + String(tag);
    aux1.toCharArray(aux, 15);
    
    // Realiza o POST
    int i = client.post(RECURSO_POST, aux);
    Serial.println("Codigo de resposta do web service: " + String(i));
    
    // Processa o retorno
    if (i == 201) {
      Serial.println("TAG enviada com sucesso.");
      sucesso();
    } else {
      Serial.println("Falha ao enviar TAG.");
      falha();      
    }
    Serial.println();
}

void sucesso() {
  // Acende o LED verde por 1 segundo
  digitalWrite(PINO_LED_VERDE, HIGH);
  delay(1000);
  digitalWrite(PINO_LED_VERDE, LOW);  
}

void falha() {
  // Acende o LED vermelho por 1 segundo
  digitalWrite(PINO_LED_VERMELHO, HIGH);
  delay(1000);
  digitalWrite(PINO_LED_VERMELHO, LOW);  
}
