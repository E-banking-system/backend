package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.services.authentification.AuthentificationService;
import adria.sid.ebanckingbackend.exceptions.UserNotEnabledException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthentificationService authenticationService;

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(@RequestBody @Valid AuthReqDTO request) {
    try {
      AuthResDTO response = authenticationService.authenticate(request);
      if (response != null) {
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    } catch (UserNotEnabledException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not verified. Please check your email for verification instructions.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    authenticationService.refreshToken(request, response);
  }
}
