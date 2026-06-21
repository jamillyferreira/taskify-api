package com.ferreira.taskify_api.controller;

import com.ferreira.taskify_api.dto.request.ProfileUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.UserResponseDTO;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponseDTO response = profileService.getMyProfile(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            Authentication authentication,
            @RequestBody @Valid ProfileUpdateRequestDTO request) {

        User user = (User) authentication.getPrincipal();
        UserResponseDTO response = profileService.updateMyProfile(user, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        profileService.deleteMyAccount(user);
        return ResponseEntity.noContent().build();
    }
}
