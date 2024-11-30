package IOT_house.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LedController {

    private boolean ledStatus = false; // LED OFF ban đầu
    private double temperature = 0.0; // Nhiệt độ giả định
    private double humidity = 0.0;    // Biến lưu trữ độ ẩm

    // API để lấy trạng thái LED
    @GetMapping("/ledStatus")
    public ResponseEntity<Map<String, Integer>> getLedStatus() {
        Map<String, Integer> response = new HashMap<>();
        response.put("ledStatus", ledStatus ? 1 : 0);
        return ResponseEntity.ok(response);
    }

    // API để toggle LED
    @PostMapping("/toggleLedFromWeb")
    public ResponseEntity<Map<String, Integer>> toggleLedFromWeb() {
        ledStatus = !ledStatus; // Toggle LED
        Map<String, Integer> response = new HashMap<>();
        response.put("ledStatus", ledStatus ? 1 : 0);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/updateSensorData")
    public ResponseEntity<String> updateSensorData(@RequestBody String jsonData) {
        // Giải mã JSON từ ESP
        try {
            // Đọc giá trị temperature và humidity từ JSON
            String[] parts = jsonData.replaceAll("[{}\"]", "").split(",");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue[0].trim().equals("temperature")) {
                    temperature = Double.parseDouble(keyValue[1].trim());
                } else if (keyValue[0].trim().equals("humidity")) {
                    humidity = Double.parseDouble(keyValue[1].trim());
                }
            }
            return ResponseEntity.ok("Sensor data received and processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid data format");
        }
    }
    // API để lấy giá trị nhiệt độ từ ESP
    @GetMapping("/getTemperature")
    public ResponseEntity<Map<String, Double>> getTemperature() {
        Map<String, Double> response = new HashMap<>();
        response.put("temperature", temperature); // Trả về nhiệt độ
        response.put("humidity", humidity); // Trả về độ ẩm
        return ResponseEntity.ok(response);
    }

    
}
