/*
 * Este é o sketch do Arduino Uno responsável por enviar os dados do RFID
 * pela porta serial para o Arduino Mega.
 * 
 * Neste Arduino os pinos 0 e 1 que são RX e TX, respectivamente. Eles são
 * conectados nos mesmos pinos 18 e 19 do Arduino Mega (RX -> TX, TX -> RX).
 *
 * O código é baseado no post "CONTROLE DE ACESSO USANDO LEITOR RFID COM ARDUINO"
 * de ADILSON THOMSEN, disponível em 
 * <http://blog.filipeflop.com/wireless/controle-acesso-leitor-rfid-arduino.html>. 
 */
#include <SPI.h>
#include <MFRC522.h>

 char tag[9];

#define INTERVALO_LEITURAS 2000

// Pinos do RFID
#define SS_PIN 10
#define RST_PIN 9

// Biblioteca RFID
MFRC522 mfrc522(SS_PIN, RST_PIN);

 void setup() {
  Serial.print("Iniciando Uno...");
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
}

void loop() {
  // Verifica se há um novo cartão
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    return;
  }

  // Verifica se um cartão foi passado para leitura
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }
  
  // Realiza a leitura do cartão
  String conteudo= "";
  byte letra;
  for (byte i = 0; i < mfrc522.uid.size; i++) {
   conteudo.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : ""));
   conteudo.concat(String(mfrc522.uid.uidByte[i], HEX));
 }
 
 enviarDados(conteudo);
 delay(INTERVALO_LEITURAS);
}

void enviarDados(String value){
  value.toCharArray(tag, 9);
  Serial.write(tag, 9);
}
