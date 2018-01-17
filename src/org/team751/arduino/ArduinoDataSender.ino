/**
   Author: Kenny Akers
   Organization: Team 751 Barn2 Robotics
   Competition: 2018 FRC
   Email: kakers19@priorypanther.com

   This Arduino program establishes a Serial communication protocol with the RoboRIO.
   Currently, it is programmed to work with queries of the format:
                  <query_token><separator_token><request_ID>
                  Ex. Q-123

   And to respond with the following format:
                  <request_ID><separator><rotation_data><separator><distance_traveled><separator><velocity_data>
                  Ex. 123-130-42-80

   The Arduino will send this data 3 times unless it receives the confirmation token from the RoboRIO, at which
   point it will stop. If it does not receive this token before it has sent the data 3 times, it will stop.
*/

unsigned long requestID; // Variable to hold the query ID of the incoming request.
String separator = "-"; // Character used to separate command from ID number.
String queryToken = "Q"; // Token used by the RoboRIO to initiate a request.
String confirmationToken = "OK"; // Token used by the RoboRIO signal that it successfully received the correct response to the query (i.e. both request and response IDs match).
int sendRepeat = 10; // Variable that denotes how many times the Arduino will send the queried information if it does not receive the confirmation token.

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(10);
}

void loop() {
  if (Serial.available() > 0) { // If the RoboRIO has sent a query command...
    String request = Serial.readString();
    //Serial.println(request);
    if (request.startsWith(queryToken + separator)) {
      request = request.substring(request.indexOf(separator) + 1);
      requestID = request.toInt();
      //Serial.println(requestID);
      sendData();
    }
  }
}

void sendData() {
  int count = 0; // Counter so the sensor data is only sent a maximum of 3 times.
  while (count++ < sendRepeat && !Serial.readString().equals(confirmationToken + separator + String(requestID))) { // While the RoboRIO hasn't sent the right verification token.
    Serial.print(requestID);
    Serial.print("-");
    Serial.print(getRotation());
    Serial.print("-");
    Serial.print(getDistanceTraveled());
    Serial.print("-");
    Serial.println(getVelocity());
    delay(2000);
  }
}

int getRotation() {
  // Rotation data
}

int getDistanceTraveled() {
  // Distance data
}

int getVelocity() {
 // Velocity data
}
