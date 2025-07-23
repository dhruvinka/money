package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.EmailRequest;
import com.example.expenssManeger.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {


    private final EmailService emailService;


    @PostMapping("/send")
    public String sendEmailWithAttachment(@RequestParam String to,
                                          @RequestParam String subject,
                                          @RequestParam String message,
                                          @RequestParam("file") MultipartFile file) throws Exception {
        emailService.sendEmailWithAttachment(to, subject, message, file.getBytes(), file.getOriginalFilename());
        return "Email sent successfully with attachment.";
    }


}
