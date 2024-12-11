package IOT_house.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import IOT_house.entity.Equipments;
import IOT_house.services.admin.EquipmentService;

@RestController
@RequestMapping("/api")
public class LedController {
	@Autowired
	EquipmentService equipService;
    private boolean ledStatus2 = false; // LED OFF ban đầu
    private boolean ledStatus3 = false; // LED OFF ban đầu
    private boolean ledStatus4 = false; // LED OFF ban đầu
    private boolean ledStatus5 = false; // LED OFF ban đầu
    private double temperature = 0.0; // Nhiệt độ giả định
    private double humidity = 0.0;    // Biến lưu trữ độ ẩm

 // API để lấy trạng thái LED cho thiết bị cụ thể
    @GetMapping("/ledStatus")
    public ResponseEntity<Map<String, Object>> getLedStatus() {
        // Giả sử bạn có một phương thức getLedStatusByEquipId để lấy trạng thái LED của thiết bị theo id

        Map<String, Object> response = new HashMap<>();
        response.put("ledStatus2", ledStatus2 ? 1 : 0);  // Trạng thái LED (1: ON, 0: OFF)
        response.put("ledStatus3", ledStatus3 ? 1 : 0);  // Trạng thái LED (1: ON, 0: OFF)
        response.put("ledStatus4", ledStatus4 ? 1 : 0); 
        response.put("ledStatus5", ledStatus5 ? 1 : 0); 
        return ResponseEntity.ok(response);
    }
    
    // API để toggle LED
    @PostMapping("/toggleLed/{equipId}")
    public ResponseEntity<Map<String, Object>> toggleLedFromWeb(@PathVariable Long equipId) {
        
		// Lấy thiết bị theo ID
        Optional<Equipments> equipment = equipService.findById(equipId);
        Map<String, Object> response = new HashMap<>();
       if (equipId == 2){
    	   ledStatus2 =!ledStatus2;
           response.put("ledStatus"+equipId, ledStatus2 ? 1 : 0); // Trạng thái LED mới (1: ON, 0: OFF)
       }
       else if (equipId == 3) {
    	   ledStatus3 =!ledStatus3;
           response.put("ledStatus"+equipId, ledStatus3 ? 1 : 0); // Trạng thái LED mới (1: ON, 0: OFF)
       }
       else if (equipId == 4) {
    	   ledStatus4 =!ledStatus4;
    	   response.put("ledStatus"+equipId, ledStatus4 ? 1 : 0); // Trạng thái LED mới (1: ON, 0: OFF)
       }
       else if (equipId == 5) {
    	   ledStatus5 =!ledStatus5;
    	   response.put("ledStatus"+equipId, ledStatus5 ? 1 : 0); // Trạng thái LED mới (1: ON, 0: OFF)
       }
        // Tạo phản hồi
       
        response.put("id", equipment.get().getId());
        response.put("name", equipment.get().getSensor());
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
