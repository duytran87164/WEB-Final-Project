package IOT_house.controllers.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

 // Hàm gửi email đặt lại mật khẩu
    public boolean sendPasswordResetEmail(String email) {
        // Tạo token để liên kết với việc đặt lại mật khẩu
        String resetToken = UUID.randomUUID().toString(); 

        // Tạo một email MIME (cho phép bạn gửi email với định dạng HTML)
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            // Thiết lập email
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText("To reset your password, click the link below:\n" +
                           "http://localhost:8080/reset-password?token=" + resetToken, true);  // true để gửi email dạng HTML

            // Gửi email
            mailSender.send(message);

            // Nếu gửi email thành công, bạn có thể lưu lại token vào cơ sở dữ liệu
            // Ví dụ: userService.savePasswordResetToken(email, resetToken); 

            return true;
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}