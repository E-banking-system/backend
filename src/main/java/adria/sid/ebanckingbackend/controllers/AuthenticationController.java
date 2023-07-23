package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterBanquierDTO;
import adria.sid.ebanckingbackend.dtos.ReqRegisterClientDTO;
import adria.sid.ebanckingbackend.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/client/register")
  public ResponseEntity<AuthResDTO> registerClient(
          @RequestBody ReqRegisterClientDTO request
  ) {
    return ResponseEntity.ok(service.registerClient(request));
  }

  @PostMapping("/banquier/register")
  public ResponseEntity<AuthResDTO> registerBanquier(
          @RequestBody ReqRegisterBanquierDTO request
  ) {
    return ResponseEntity.ok(service.registerBanquier(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthResDTO> authenticate(@RequestBody AuthReqDTO request) {
    try {
      AuthResDTO response = service.authenticate(request);
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
    service.refreshToken(request, response);
  }

}
