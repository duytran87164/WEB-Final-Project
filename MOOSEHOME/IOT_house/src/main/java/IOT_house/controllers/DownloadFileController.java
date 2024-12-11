package IOT_house.controllers;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class DownloadFileController {

    public static final String DIR = "D:\\upload";

    @GetMapping("/image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam("fname") String fileName) throws IOException {
        File file = new File(DIR + File.separator + fileName);

        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/video")
//    public ResponseEntity<InputStreamResource> getVideo(@RequestParam("fname") String fileName) throws IOException {
//        File file = new File(DIR + File.separator + fileName);
//
//        if (file.exists()) {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            InputStreamResource resource = new InputStreamResource(fileInputStream);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // Use MediaType.APPLICATION_OCTET_STREAM for mp4
//
//            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
