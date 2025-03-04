package br.com.gfctech.project_manager.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.gfctech.project_manager.dto.UserDTO;
import br.com.gfctech.project_manager.entity.UserEntity;
import br.com.gfctech.project_manager.entity.UserEntity.Role;
import br.com.gfctech.project_manager.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public List<UserDTO> getAllUsers() {
		List<UserEntity> users = userRepository.findAll(); // Obtém todos os usuários do repositório
		return users.stream() // Cria um fluxo de usuários
					.map(user -> new UserDTO(user)) // Mapeia cada usuário para um UserDTO
					.collect(Collectors.toList()); // Coleta os DTOs em uma lista
	}

    public void addUser(UserDTO addUser) {

        String senhaGerada = RandomStringUtils.randomAlphanumeric(8); // Gera uma senha aleatória
        // Cria a entidade do usuário
        UserEntity user = new UserEntity();
        user.setName(addUser.getName());
        user.setLogin(addUser.getLogin());
        user.setEmail(addUser.getEmail());
        user.setRole(Role.valueOf(addUser.getRole()));
        user.setPassword(passwordEncoder.encode(senhaGerada));
        userRepository.save(user);

        // Envia a senha no email para o usuário
        String assunto = "Login no HardProject";
        String mensagem = "Olá Sr(a). " + user.getName() + ",\n\n"
                + "Seu cadastro foi realizado com sucesso no HardProject. Segue abaixo sua senha para entrar no sistema:\n\n"
                + "Senha: " + senhaGerada + "\n\n"
                + "Recomendamos que você altere sua senha após o primeiro login.";

        emailService.enviarEmailTexto(user.getEmail(), assunto, mensagem);
    }
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setName(userDTO.getName());
        existingUser.setLogin(userDTO.getLogin());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setRole(Role.valueOf(userDTO.getRole())); 
        existingUser.setPassword(userDTO.getPassword());
        // Atualize outros campos conforme necessário
        return new UserDTO(userRepository.save(existingUser));
    }

    @Transactional
    public UserDTO updatePermissions(Long id, String role) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        userRepository.delete(user);
    }
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
            .map(UserDTO::new) // Usa o construtor corrigido
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}

