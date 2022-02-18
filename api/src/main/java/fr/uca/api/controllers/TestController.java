package fr.uca.api.controllers;

import fr.uca.api.models.ERole;
import fr.uca.api.util.VerifyAuthorizations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payload.response.MessageResponse;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity userAccess(@RequestHeader Map<String, String> headers) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_STUDENT.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        return ResponseEntity.ok().body(new MessageResponse("User Content."));
    }

    @PostMapping("/mod")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity moderatorAccess(@RequestHeader Map<String, String> headers) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        return ResponseEntity.ok().body(new MessageResponse("Teacher Board."));
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity adminAccess(@RequestHeader Map<String, String> headers) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_ADMIN.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        return ResponseEntity.ok().body(new MessageResponse("Admin Board."));
    }
}
