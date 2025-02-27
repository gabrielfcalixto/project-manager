package br.com.gfctech.project_manager.entity;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.beans.BeanUtils;

import br.com.gfctech.project_manager.dto.UserDTO;
import br.com.gfctech.project_manager.entity.enums.TipoSituacaoUsuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GFC_USER")
@NoArgsConstructor
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) // Armazena a role como texto no banco
    @Column(nullable = false)
    private Role role; 

    @Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoSituacaoUsuario situacao;
	

    @Column
    private LocalDate joinDate;

 	//construtor
	public UserEntity(UserDTO user) {
		BeanUtils.copyProperties(user, this);
	}

   
	

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    
    public TipoSituacaoUsuario getSituacao() {
        return situacao;
    }



    public void setSituacao(TipoSituacaoUsuario situacao) {
        this.situacao = situacao;
    }



    // Enum de Role dentro da entidade
    public enum Role {
        ADMIN, MANAGER, USER, ;
    }

    @Override
	public int hashCode() {
		return Objects.hash(id);
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		return Objects.equals(id, other.id);
	}




}
