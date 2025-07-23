package com.example.expenssManeger.Controller;

import com.example.expenssManeger.dto.AuthDto;
import com.example.expenssManeger.dto.ProfileDTO;
import com.example.expenssManeger.entity.ProfileEntity;
import com.example.expenssManeger.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {


    private  final ProfileService profileService;


    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> regidter(@RequestBody ProfileDTO profileDTO)
    {
        ProfileDTO registerProfile=profileService.registerprofile(profileDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }



    @GetMapping("/activate")
    public  ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean profileEntity = profileService.activateProfile(token);
        if (profileEntity) {
            return ResponseEntity.ok("Account activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid activation token.");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDto authDto)
    {
        try
        {
            if (profileService.isAccountActive(authDto.getEmail()))
            {
            Map<String,Object> res= profileService.authenticateAndGenerateToken(authDto);
                System.out.printf(res.toString());
            return ResponseEntity.ok(res);
            }
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is not active. Please activate your account first."));
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid credentials. Please try again."));

        }
    }


    @GetMapping("/test")
    public  String test()
    {
        return "hello world";
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile() {
        ProfileDTO profile = profileService.getPublicProfile(null);

            return ResponseEntity.ok(profile);

    }
}
