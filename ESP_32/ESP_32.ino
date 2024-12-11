#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>  // Thêm thư viện ArduinoJson
#include <DHTesp.h>       // Thêm thư viện DHTesp

const char* ssid = "Xuong Rang Coffee";
const char* password = "";
const String serverURL = "http://172.28.31.236:9090/api"; // Địa chỉ server

// Cấu hình cảm biến DHT
#define DHTPIN 17      
DHTesp dht;         // Khởi tạo đối tượng DHTesp

// Cấu hình LED (GPIO pin của LED)
const uint8_t ledPin2 = 0; // GPIO2 cho LED
int ledPin3 = 2;  
int ledPin4 = 4;
int ledPin5 = 15;

void setup() {
  Serial.begin(115200);
  pinMode(ledPin2, OUTPUT);
  pinMode(ledPin3, OUTPUT);
  pinMode(ledPin4, OUTPUT);
  pinMode(ledPin5, OUTPUT);
  // Khởi tạo cảm biến DHT
  dht.setup(DHTPIN, DHTesp::DHT11); 
  
  // Kết nối Wi-Fi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) delay(1000);
  Serial.println("Connected to WiFi");
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    String payload = "";

    // Mảng equipIds cần quét

      String url = serverURL + "/ledStatus";  // Tạo URL cho từng equipId

      // Gửi yêu cầu GET cho từng thiết bị để lấy trạng thái LED
      http.begin(url);
      int httpCode = http.GET();  // Gửi yêu cầu GET

      if (httpCode == 200) {
        payload = http.getString();  // Lấy chuỗi JSON trả về
        Serial.println("Received response for equipId: " );
      } else {
        Serial.println("Failed to get data for equipId: " );
      }
      http.end();

      // Phân tích JSON và điều khiển LED cho mỗi thiết bị
      StaticJsonDocument<200> doc;
      if (deserializeJson(doc, payload) == DeserializationError::Ok) {
        // Đọc trạng thái LED cho mỗi thiết bị
        int ledStatus2 = doc["ledStatus2"];  
        int ledStatus3 = doc["ledStatus3"];  
        int ledStatus4 = doc["ledStatus4"];
        int ledStatus5 = doc["ledStatus5"];  
        // Điều khiển LED 14
        if (ledStatus2 == 1) {
          digitalWrite(ledPin2, HIGH);  // Bật LED
          Serial.println("LED 2 is ON");
        } else {
          digitalWrite(ledPin2, LOW);   // Tắt LED
          Serial.println("LED 2 is OFF");
        }

        if (ledStatus3 == 1) {
          digitalWrite(ledPin3, HIGH);  
          Serial.println("LED 3 is ON");
        } else {
          digitalWrite(ledPin3, LOW);   
          Serial.println("LED 3 is OFF");
        }
        if (ledStatus4 == 1) {
          digitalWrite(ledPin4, HIGH);  // Bật LED
          Serial.println("LED 4 is ON");
        } else {
          digitalWrite(ledPin4, LOW);   // Tắt LED
          Serial.println("LED 4 is OFF");
        }
        if (ledStatus5 == 1) {
          digitalWrite(ledPin5, HIGH);  // Bật LED
          Serial.println("LED 5 is ON");
        } else {
          digitalWrite(ledPin5, LOW);   // Tắt LED
          Serial.println("LED 5 is OFF");
        }
      }


    // Đọc nhiệt độ và độ ẩm từ cảm biến DHT
    float temperature = dht.getTemperature(); // Đọc nhiệt độ (C)
    float humidity = dht.getHumidity(); // Đọc độ ẩm (%)

    // Kiểm tra nếu giá trị đọc được hợp lệ
    if (isnan(temperature) || isnan(humidity)) {
      Serial.println("Failed to read from DHT sensor!");
    } else {
      // Gửi POST request để cập nhật nhiệt độ và độ ẩm lên server
      http.begin(serverURL + "/updateSensorData");
      String jsonData = "{\"temperature\": " + String(temperature) + ", \"humidity\": " + String(humidity) + "}";
      http.addHeader("Content-Type", "application/json");
      int postCode = http.POST(jsonData);  // Gửi yêu cầu POST

      if (postCode == 200) {
        Serial.println("Successfully sent temperature and humidity data.");
      } else {
        Serial.println("Failed to send temperature and humidity data.");
      }
      http.end();  // Kết thúc kết nối HTTP

      // In ra giá trị nhiệt độ và độ ẩm
      Serial.print("Temperature: ");
      Serial.print(temperature);
      Serial.print(" °C, Humidity: ");
      Serial.print(humidity);
      Serial.println(" %");
    }
  }

}

