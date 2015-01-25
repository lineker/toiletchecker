// We name pin D0 as led
int led = D7; 
int input = D6;
int status;

char *message = "my name is toilet checker";
// This routine runs only once upon reset
void setup() 
{
  Serial.begin(9600);
  // Initialize D0 pin as an output
  pinMode(led, OUTPUT);
  pinMode(input, INPUT);
}

// This routine loops forever 
void loop() 
{
  status = digitalRead(input);
  digitalWrite(led, status);   // Turn ON the LED

// variable name max length is 12 characters long
  Spark.variable("status", &status, INT);
  Spark.variable("mess", message, STRING);
  Serial.println(status);

  delay(300);
  
}

