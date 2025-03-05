package br.com.gfctech.project_manager.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // Recupera o usuário pelo ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + userId));
        
        // Verifica se a senha antiga fornecida está correta
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("A senha antiga fornecida está incorreta.");
        }

        // Verifica se a nova senha é diferente da atual
        if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("A nova senha deve ser diferente da senha antiga.");
        }

        // Codifica a nova senha
        user.setPassword(passwordEncoder.encode(newPassword));

        // Salva o usuário com a nova senha
        userRepository.save(user);

        // Envia e-mail de confirmação (opcional)
        String assunto = "Senha alterada com sucesso";
        String mensagem = "Olá " + user.getName() + ",\n\n"
                + "Sua senha foi alterada com sucesso.\n\n"
                + "Se você não solicitou essa alteração, entre em contato conosco imediatamente.";

        emailService.enviarEmailTexto(user.getEmail(), assunto, mensagem);
    }


    @Transactional
    public String uploadProfilePicture(Long id, MultipartFile file) {
        try {
            // Encontra o usuário pelo ID
            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Converte o arquivo para um array de bytes
            byte[] profilePicture = file.getBytes();
            user.setProfilePicture(profilePicture);

            // Salva o usuário com a nova foto de perfil
            userRepository.save(user);

            return "Foto de perfil atualizada com sucesso!";
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem no banco de dados.", e);
        }
    }

    // Método para recuperar a foto de perfil
    public byte[] getProfilePicture(Long userId) {
        // Recupera o usuário do banco de dados
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        byte[] profilePicture = user.getProfilePicture();

        if (profilePicture == null || profilePicture.length == 0) {
            return null; // Retorna null se não houver imagem
        }

        return profilePicture;
    }
}



