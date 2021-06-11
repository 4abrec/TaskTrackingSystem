package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.task.track.config.jwt.JwtUtils;
import web.task.track.domain.ERole;
import web.task.track.domain.Role;
import web.task.track.domain.User;
import web.task.track.dto.response.JwtResponse;
import web.task.track.dto.LoginDto;
import web.task.track.dto.response.MessageResponse;
import web.task.track.dto.RegistrationDto;
import web.task.track.repository.RoleRepository;
import web.task.track.repository.UserRepository;
import web.task.track.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /*
    Авторизация пользователя.
     */
    @Override
    public ResponseEntity<JwtResponse> login(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /*
    Регистрация пользователя. Сначала идет проверка на наличие текущего пользователя в базе данных.
    Затем пользоватею устанавляиваются роли.
     */
    @Override
    public ResponseEntity<MessageResponse> registration(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is exist"));
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is exist"));
        }

        User user = new User(registrationDto.getUsername(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getFirstName(), registrationDto.getLastName());

        Set<String> reqRoles = registrationDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(role -> {
                switch (role) {
                    case "manager":
                        Role roleManager = roleRepository
                                .findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error, Role MANAGER is not found"));
                        roles.add(roleManager);
                        break;

                    case "developer":
                        Role roleDeveloper = roleRepository
                                .findByName(ERole.ROLE_DEVELOPER)
                                .orElseThrow(() -> new RuntimeException("Error, Role DEVELOPER is not found"));
                        roles.add(roleDeveloper);
                        break;

                    case "tester":
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_TESTER)
                                .orElseThrow(() -> new RuntimeException("Error, Role TESTER is not found"));
                        roles.add(modRole);
                        break;

                    default:
                        throw new RuntimeException("Error, Role " + role + " is not found");
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
