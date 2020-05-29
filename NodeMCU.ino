#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <RCSwitch.h>
#include <Arduino.h>
#include <IRremoteESP8266.h>
#include <IRsend.h>

#include "CONFIGURATIONS.h" //depands network konfiguration data

//RF and IR code-data
const long RCSWITCHCODE[][2]      =   {{280608, 1045296}, {12494204, 11940012}, {772380, 1045308}, {1045298, 772370}}; //{230816 (fehlerhaft),1045300(ok)}
const unsigned long IRCOLORCODE[] =   {0xF7A05F, 0xF7906F, 0xF7B04F, 0xF78877, 0xF7A857, 0xF7609F, 0xF750AF, 0xF7708F, 0xF748B7, 0xF76897, 0xF720DF, 0xF710EF, 0xF730CF, 0xF708F7, 0xF728D7};
const unsigned long IRCCODE[]     =   {0xF740BF, 0xF7C03F, 0xF7E01F, 0xF7807F, 0xF700FF};

//Network configuration
IPAddress subnet(255, 255, 255, 0);         
ESP8266WebServer server(80);    //server port

RCSwitch mySwitch = RCSwitch(); //433Mhz modul
IRsend irsend(4); 

void setup() {
  Serial.begin(115200);
  delay(100);                   //10ms waiting for serial begin
  Serial.println("");
  Serial.print("try to connect with wifi");

  //connect with wifi
  WiFi.mode(WIFI_STA);
  WiFi.begin(WLANSSID, WLANPASSWORD);
  WiFi.config(IP, GATEWAY, subnet);
  while (WiFi.status() != WL_CONNECTED) { 
    delay(500);
    Serial.print(".");
  }
  Serial.println(" finished!");
  Serial.print(">ip: ");
  Serial.println(WiFi.localIP());
  Serial.print(">mac: ");
  Serial.println(WiFi.macAddress());
  
  //if servlet "/do" is requested the "callDo"-method starts.
  server.on("/do", callDo);
  server.begin(); 

  //Setup 433Mhz Sender
  mySwitch.enableTransmit(5);
  mySwitch.setProtocol(4);
  mySwitch.setPulseLength(355);
  //mySwitch.setRepeatTransmit(15);

  //Setup IR Sender
  irsend.begin();
}

void loop() {
  server.handleClient();
}

void callDo() {
  char log_string[40] = "";
  for (int i = 0; i < server.args(); i++) {
    
    //get argument and order
    String argument = server.argName(i);
    String parameterValue = server.arg(i);

    //execute orders
    if (argument == "switch") {
      setSwitch((parameterValue[0] - '0'), (parameterValue[2] - '0'));
      strcat(log_string, argument.c_str());
      strcat(log_string, parameterValue.c_str());
      strcat(log_string, " wurde geschaltet");
    }
    if (argument == "colorled") {
      setRGBLed(parameterValue.toInt(),true);
      strcat(log_string, argument.c_str());
      strcat(log_string, parameterValue.c_str());
      strcat(log_string, " wurde geschaltet");
    }
    if (argument == "cled") {
      setRGBLed(parameterValue.toInt(),false);
      strcat(log_string, argument.c_str());
      strcat(log_string, parameterValue.c_str());
      strcat(log_string, " wurde geschaltet");
    }
  }

  Serial.println(log_string);
  sendResult(log_string);
}

//send result, wich will be displayed in android app
void sendResult(String content) {
  //200 ist die Antwort das alles OK ist, text/html ist der MimeType
  server.send(200, "text/html", content);
}

//send 433 mhz signal
void setSwitch(int b, int p) {
  mySwitch.send(RCSWITCHCODE[b][p], 24);
}

//send ir signal
void setRGBLed(int b, bool color){
  if(color){
    irsend.sendNEC(IRCOLORCODE[b]);
  }else{
    irsend.sendNEC(IRCCODE[b]);
  }

}
