package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.authentification.AuthReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.AuthResDTO;
import adria.sid.ebanckingbackend.dtos.authentification.ChangeOperateurReqDTO;
import adria.sid.ebanckingbackend.dtos.authentification.UserInfosResDTO;
import adria.sid.ebanckingbackend.dtos.client.ClientResDTO;
import adria.sid.ebanckingbackend.exceptions.IdUserIsNotValideException;
import adria.sid.ebanckingbackend.exceptions.UserHasNotAnyCompte;
import adria.sid.ebanckingbackend.services.authentification.AuthenticationService;
import adria.sid.ebanckingbackend.exceptions.UserNotEnabledException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PutMapping("/operateur")
  public ResponseEntity<?> updateOperateur(@RequestBody @Valid ChangeOperateurReqDTO changeOperateurReqDTO) {
    try {
      Boolean success = authenticationService.changeOperateur(changeOperateurReqDTO);
      if (success) {
        return ResponseEntity.ok().body("Your operateur field is changed with success");
      } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update operateur.");
      }
    } catch (IdUserIsNotValideException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
  }


  @GetMapping("/infos")
  public ResponseEntity<?> getClients(
          @RequestParam String userId
  ) {
    try {
      UserInfosResDTO userInfosResDTO = authenticationService.getUserInfos(userId);
      return ResponseEntity.ok(userInfosResDTO);
    } catch (InternalError e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    } catch (IdUserIsNotValideException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(@RequestBody @Valid AuthReqDTO request) {
    try {
      AuthResDTO response = authenticationService.authenticate(request);
      return ResponseEntity.ok(response);
    } catch (UserNotEnabledException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
              .body("User not verified. Please check your email for verification instructions.");
    } catch (UserHasNotAnyCompte e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("You must visit your bank to create a banking account.");
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body("Invalid email or password.");
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body("User not found.");
    } catch (RuntimeException e) {
      // This is a catch-all for unexpected runtime exceptions.
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("An unexpected error occurred.");
    }
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    authenticationService.refreshToken(request, response);
  }

  // Exception handler to handle RuntimeException
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  // Exception handler to handle UsernameNotFoundException
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  // Exception handler to handle BadCredentialsException
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  // Exception handler to handle UserHasNotAnyCompte
  @ExceptionHandler(UserHasNotAnyCompte.class)
  public ResponseEntity<String> handleUserHasNotAnyCompte(UserHasNotAnyCompte e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  // Exception handler to handle UserNotEnabledException
  @ExceptionHandler(UserNotEnabledException.class)
  public ResponseEntity<String> handleUserNotEnabledException(UserNotEnabledException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
  }

}
