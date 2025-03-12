package br.com.gfctech.project_manager.service;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.gfctech.project_manager.entity.UserEntity;

public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private UserEntity user;


    public UserDetailsImpl(Long id, String name, String login, String password, String email,
                           Collection<? extends GrantedAuthority> authorities, UserEntity user) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.user = user;

    }

    public static UserDetailsImpl build(UserEntity user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(user.getRole().name()));

        return new UserDetailsImpl(
            user.getId(), 
            user.getName(), 
            user.getLogin(),
            user.getPassword(),
            user.getEmail(), 
            authorities,
            user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    
    }

    public String getJoinDate() {
    if (user.getJoinDate() != null) {
        // Formata o LocalDate para uma String no formato "dd-MM-yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return user.getJoinDate().format(formatter);
    }
    return null; 
}

}