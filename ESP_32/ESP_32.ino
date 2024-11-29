#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>  // Thêm thư viện ArduinoJson
#include <DHTesp.h>       // Thêm thư viện DHTesp

// Cấu hình thông tin Wi-Fi
const char* ssid = "Xuong Rang Coffee";
const char* password = "";
const String serverURL = "http://172.28.30.117:9090/api"; // Địa chỉ server

// Cấu hình cảm biến DHT
#define DHTPIN 17      // Chân DHT (GPIO4 trong ví dụ này)
DHTesp dht;         // Khởi tạo đối tượng DHTesp

// Cấu hình LED (GPIO pin của LED)
const uint8_t ledPin = 2; // GPIO2 cho LED

void setup() {
  Serial.begin(115200);
  pinMode(ledPin, OUTPUT);
  
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

    // Gửi GET request để lấy trạng thái LED
    http.begin(serverURL + "/ledStatus");
    if (http.GET() == 200) payload = http.getString();
    http.end();

    // Phân tích JSON và điều khiển LED
    StaticJsonDocument<200> doc;
    if (deserializeJson(doc, payload) == DeserializationError::Ok) {
      digitalWrite(ledPin, doc["ledStatus"] == 1 ? HIGH : LOW);
      Serial.println(doc["ledStatus"] == 1 ? "LED ON" : "LED OFF");
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
      http.POST(jsonData);  // Gửi dữ liệu dưới dạng JSON
      http.end();

      // In ra giá trị nhiệt độ và độ ẩm
      Serial.print("Temperature: ");
      Serial.print(temperature);
      Serial.print(" °C, Humidity: ");
      Serial.print(humidity);
      Serial.println(" %");
    }

    // Gửi POST request để thay đổi trạng thái LED
    http.begin(serverURL + "/toggleLed");
    http.POST("");  // Gửi POST mà không cần payload
    http.end();
  }
}
