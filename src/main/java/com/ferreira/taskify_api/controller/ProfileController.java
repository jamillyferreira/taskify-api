package com.ferreira.taskify_api.controller;

import com.ferreira.taskify_api.doc.ProfileControllerDoc;
import com.ferreira.taskify_api.dto.request.user.ProfileUpdateRequestDTO;
import com.ferreira.taskify_api.dto.response.user.UserResponseDTO;
import com.ferreira.taskify_api.model.User;
import com.ferreira.taskify_api.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController implements ProfileControllerDoc {
    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal User user) {
        UserResponseDTO response = profileService.getMyProfile(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ProfileUpdateRequestDTO request) {
        UserResponseDTO response = profileService.updateMyProfile(user, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(@AuthenticationPrincipal User user) {
        profileService.deleteMyAccount(user);
        return ResponseEntity.noContent().build();
    }
}
