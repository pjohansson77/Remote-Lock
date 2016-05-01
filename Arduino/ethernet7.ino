#include <SPI.h>
#include <Ethernet.h>
#include <LiquidCrystal.h>
// Enter a MAC address, IP address and Portnumber below for your server.
// The IP address will be dependent on your local network.
byte mac[] = {0x90, 0xA2, 0xDA, 0x0F, 0x09, 0x01};
IPAddress serverIP(10,228,0,123);
int serverPort = 6666;
int input;
boolean unlock = false;
boolean doorNotClosed;

// Decides which pins goes to which led
int ledRed = 7; // red
int ledGreen = 6; //green

// String variables
String unLocked = "unlocked";
String openDoor = "not closed";
String locked = "locked";

// Initiates the LCD display
LiquidCrystal lcd(9 ,8 ,5 ,4 ,3 ,2);

// Initiates the ethernet server with a port number
EthernetServer server = EthernetServer(serverPort);
    
void setup() {
  // Starts the ethernet servern
  Ethernet.begin(mac, serverIP);
  
  // Turns pin 6,7 to an output
  pinMode(ledRed, OUTPUT);
  pinMode(ledGreen, OUTPUT);
  pinMode(1, INPUT);
  pinMode(A5, INPUT);
  
  server.begin();
      
  // Starts the LCD with a 2x16 row in 4-bits mode
  lcd.begin(16, 2);
}
    
void loop() { 
  // Initiates an ethernet client
  EthernetClient client = server.available();
  int power = analogRead(A5);

  // If the power supply for the lock goes down, 
  // "Power down" is written on the display.  
  if(power < 500){ // Power down
   lcd.clear();
   lcd.print("Power down");
   unlock = false;
   digitalWrite(ledGreen, LOW);
   digitalWrite(ledRed, HIGH);
   server.write(5);
  }else{
   lcd.clear();
  }    
 
  // If there is a client and there is an avaliable connection
  if(client) {
    while(client.connected()){
      if(client.available()){
        int sensor = digitalRead(1);
        input = client.read();
        if(sensor > 0) { // Door open
          doorNotClosed = true;
        }else{
          doorNotClosed = false;       
        } 
             
        // If the message contains a 1, the lock is opened, a green led is turned
        // on and the text "The door is unlocked" is written on the LCD for 2 sec.
        if(input == 1) {
            int timeout = 0;
            server.write(1);
            digitalWrite(ledRed, LOW);
            digitalWrite(ledGreen, HIGH);
            lcd.clear();
            lcd.print("The door is");
            lcd.setCursor(0, 1);
            lcd.print(unLocked);
            unlock = true; // Door unlocked
            delay(2000);
            lcd.clear();

        }
             
         // If the message contains a 2, the lock is locked, a red led is turned
         // on and the text "The door is locked" is written on the LCD for 2 sec.  
          if(input == 2 && !doorNotClosed) {
              server.write(2);
              digitalWrite(ledGreen, LOW);
              digitalWrite(ledRed, HIGH);
              lcd.clear();
              lcd.print("The door is");
              lcd.setCursor(0, 1);
              lcd.print(locked);
              delay(2000);
              lcd.clear();
              unlock = false; //Door locked
        
            }
                
          // If a 2 is entered before the arduino updates the server to
          // door open, the display still shows the text "Door open".
          if(input == 2 && doorNotClosed) {
              server.write(3);
              lcd.print("Door open");
              delay(2000);
            }
            
          // An 8 from the server means a status query and the
          // arduino chooses a status depending on which is correct.
          if(input == 8 && doorNotClosed && power > 500) {
            server.write(3);
            lcd.print("Door open");
            delay(2000);
          }
          if(input == 8 && !doorNotClosed && unlock) {
            server.write(1);
          }
          if(input == 8 && !doorNotClosed && !unlock) {
            server.write(2);
          }
            
          if(input == 0) {
            delay(1);
            client.stop();
          }    
       }
    }
  } 
  // Automatic locking after ~20 sec if the connection with the server is lost.
  else {
    int counter = 0;
    while(counter < 200 && !client) {
      client = server.available();
      counter++;
      delay(100);
    }
    if(counter == 200){
      lcd.clear();
      digitalWrite(ledGreen, LOW);
      unlock = false;
      lcd.print("Connection lost");
      for(int i = 0; i < 5; i++){
        digitalWrite(ledRed, HIGH);
        delay(200);
        digitalWrite(ledRed, LOW);
        delay(200);
      }
      lcd.clear();
    }
  }
}

    
    
