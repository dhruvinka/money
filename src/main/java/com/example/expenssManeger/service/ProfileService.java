    package com.example.expenssManeger.service;

    import com.example.expenssManeger.Repository.ProfileRepo;
    import com.example.expenssManeger.dto.AuthDto;
    import com.example.expenssManeger.dto.ProfileDTO;
    import com.example.expenssManeger.entity.ProfileEntity;
    import com.example.expenssManeger.util.JwtUtil;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.util.Map;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class ProfileService
    {


        @Value("${activation.url}")
        private String activationUrl;
        private  final ProfileRepo profileRepo;
        private final EmailService emailService;
        private final  PasswordEncoder passwordEncoder;
        private  final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;


        public ProfileDTO registerprofile(ProfileDTO profileDTO) {
            ProfileEntity newProfile = toEntity(profileDTO);
            newProfile.setActivationToken(UUID.randomUUID().toString());
            newProfile.setPassword(passwordEncoder.encode(profileDTO.getPassword())); // ✅ Hash here
            newProfile = profileRepo.save(newProfile);

            String activationLink = activationUrl+"/activate?token=" + newProfile.getActivationToken();
            emailService.sendEmail(newProfile.getEmail(), "Activate your account", "Click: " + activationLink);

            return toDTO(newProfile);
        }

        private ProfileDTO toDTO(ProfileEntity profile) {
            return ProfileDTO.builder()
                    .id(profile.getId())
                    .fullName(profile.getFullName())
                    .email(profile.getEmail())
                    .profileImageUrl(profile.getProfileImageUrl())
                    .createdAt(profile.getCreatedAt())
                    .updatedAt(profile.getUpdatedAt())
                    .build();
        }

        public ProfileEntity toEntity(ProfileDTO profileDTO) {
            return ProfileEntity.builder()
                    .id(profileDTO.getId())
                    .fullName(profileDTO.getFullName())
                    .email(profileDTO.getEmail())
                    .profileImageUrl(profileDTO.getProfileImageUrl())
                    .createdAt(profileDTO.getCreatedAt())
                    .updatedAt(profileDTO.getUpdatedAt())
                    .build();
        }

        public boolean activateProfile(String token) {
            return profileRepo.findByActivationToken(token)
                    .map(profile -> {
                        profile.setIsActive(true);
                        profileRepo.save(profile);
                        return true;
                    }).orElse(false);
        }

        public boolean isAccountActive(String email) {
            return profileRepo.findByEmail(email)
                    .map(ProfileEntity::getIsActive)
                    .orElse(false);
        }

        public ProfileEntity getCurrentprofile() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return profileRepo.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found: " + authentication.getName()));
        }

        public ProfileDTO getPublicProfile(String email) {
            ProfileEntity user = (email == null) ? getCurrentprofile()
                    : profileRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found: " + email));
            return toDTO(user);
        }

        public Map<String, Object> authenticateAndGenerateToken(AuthDto authDto) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword())
            );
            String token = jwtUtil.generateTokenWithUsername(authDto.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authDto.getEmail())
            );
        }
    }
