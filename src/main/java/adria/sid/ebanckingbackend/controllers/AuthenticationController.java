package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Validated
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResDTO> authenticate(@RequestBody @Valid AuthReqDTO request) {
    try {
      AuthResDTO response = authenticationService.authenticate(request);
      return ResponseEntity.ok(response);
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
