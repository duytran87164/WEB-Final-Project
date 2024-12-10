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
    private boolean ledStatus2	= false;
    private boolean ledStatus3	= false;
    private boolean ledStatus4	= false;
    private boolean ledStatus5	= false;
    private double temperature	= 0.0;
    private double humidity 	= 0.0;

    @GetMapping("/ledStatus")
    public ResponseEntity<Map<String, Object>> getLedStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("ledStatus2", ledStatus2 ? 1 : 0);
        response.put("ledStatus3", ledStatus3 ? 1 : 0);
        response.put("ledStatus4", ledStatus4 ? 1 : 0); 
        response.put("ledStatus5", ledStatus5 ? 1 : 0); 
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/toggleLed/{equipId}")
    public ResponseEntity<Map<String, Object>> toggleLedFromWeb(@PathVariable Long equipId) {
        
		Optional<Equipments> equipment = equipService.findById(equipId);
		Map<String, Object> response = new HashMap<>();
	   if (equipId == 2){
		   ledStatus2 =!ledStatus2;
	       response.put("ledStatus"+equipId, ledStatus2 ? 1 : 0);
	   }
	   else if (equipId == 3) {
		   ledStatus3 =!ledStatus3;
	       response.put("ledStatus"+equipId, ledStatus3 ? 1 : 0);
	   }
	   else if (equipId == 4) {
		   ledStatus4 =!ledStatus4;
		   response.put("ledStatus"+equipId, ledStatus4 ? 1 : 0);
	   }
	   else if (equipId == 5) {
		   ledStatus5 =!ledStatus5;
		   response.put("ledStatus"+equipId, ledStatus5 ? 1 : 0);
	   }
       
        response.put("id", equipment.get().getId());
        response.put("name", equipment.get().getSensor());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateSensorData")
    public ResponseEntity<String> updateSensorData(@RequestBody String jsonData) {
        try {
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

    @GetMapping("/getTemperature")
    public ResponseEntity<Map<String, Double>> getTemperature() {
        Map<String, Double> response = new HashMap<>();
        response.put("temperature", temperature);
        response.put("humidity", humidity);
        return ResponseEntity.ok(response);
    }
}
