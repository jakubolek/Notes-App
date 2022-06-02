package com.jakubolek.notesapp.service;

import com.jakubolek.notesapp.exception.impl.RoleAlreadyExists;
import com.jakubolek.notesapp.exception.impl.RoleNotFound;
import com.jakubolek.notesapp.exception.impl.UserNotFound;
import com.jakubolek.notesapp.exception.impl.UsernameIsTakenException;
import com.jakubolek.notesapp.model.Role;
import com.jakubolek.notesapp.model.RoleFactory;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.model.UserFactory;
import com.jakubolek.notesapp.repository.RoleRepository;
import com.jakubolek.notesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFound(username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public Role saveRole(String roleName) {
        if (getRole(roleName) != null) {
            throw new RoleAlreadyExists(roleName);
        }
        Role role = RoleFactory.create(roleName);
        return roleRepository.save(role);
    }

    @Override
    public Role addRoleToUser(String username, String roleName) {
        User user = getUser(username);
        Role role = getRole(roleName);

        if (user == null) throw new UserNotFound(username);
        if (role == null) throw new RoleNotFound(roleName);

        user.addRole(role);
        return role;
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(String username, String email, String password) {

        if (getUser(username) != null) {
            throw new UsernameIsTakenException();
        }

        password = passwordEncoder.encode(password);

        Role role = roleRepository.findByName("ROLE_USER");
        User user = UserFactory.create(username, email, password, role);
        return userRepository.save(user);
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name);
    }
}
