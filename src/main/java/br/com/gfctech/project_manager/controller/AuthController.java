package br.com.gfctech.project_manager.controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import br.com.gfctech.project_manager.dto.AcessDTO;
import br.com.gfctech.project_manager.dto.AuthenticationDTO;
import br.com.gfctech.project_manager.dto.ResetPasswordDTO;
import br.com.gfctech.project_manager.exceptions.UsuarioNaoEncontradoException;
import br.com.gfctech.project_manager.secury.jwt.JwtUtils;
import br.com.gfctech.project_manager.service.UserDetailsImpl;
import br.com.gfctech.project_manager.service.UserService;
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200") // Permite requisições do frontend
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody AuthenticationDTO authDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.getLogin(), authDto.getPassword())
            );
    
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String token = jwtUtils.generateTokenFromUserDetailsImpl(userDetails);
    
            return ResponseEntity.ok(new AcessDTO(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("status", "error", "message", "Credenciais inválidas")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("status", "error", "message", "Erro interno no servidor")
            );
        }
    }

    
    @PostMapping("/generate-reset-code")
    public ResponseEntity<String> generateResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.generatePasswordResetCode(email);
            return ResponseEntity.ok("Código de reset gerado e enviado para o e-mail.");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao gerar o código de reset: " + e.getMessage());
        }
  
  }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Validated @RequestBody ResetPasswordDTO request) {
        try {
            String email = request.getEmail();
            String code = request.getCode();
            String newPassword = request.getNewPassword();

            userService.resetPassword(email, code, newPassword);
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao redefinir a senha: " + e.getMessage());
        }
    }
}