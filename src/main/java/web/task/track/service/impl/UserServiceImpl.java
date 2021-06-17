package web.task.track.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.task.track.config.jwt.JwtUtils;
import web.task.track.domain.ERole;
import web.task.track.domain.Role;
import web.task.track.domain.Task;
import web.task.track.domain.User;
import web.task.track.dto.response.JwtResponseDto;
import web.task.track.dto.LoginDto;
import web.task.track.dto.response.MessageResponseDto;
import web.task.track.dto.RegistrationDto;
import web.task.track.exception.ObjectNotFoundException;
import web.task.track.exception.constant.RoleExceptionConstants;
import web.task.track.repository.RoleRepository;
import web.task.track.repository.UserRepository;
import web.task.track.service.UserService;

import java.util.ArrayList;
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
    public User findById(Integer id) throws ObjectNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found with id: " + id));
    }

    @Override
    public User findByUsername(String username) throws ObjectNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found with username: " + username));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findUserInTaskEditHistory(List<Task> historyTasks) throws ObjectNotFoundException {
        List<User> users = new ArrayList<>();
        for (Task task: historyTasks){
            User user = findById(task.getUser().getId());
            users.add(user);
        }
        return users;
    }

    /*
        Авторизация пользователя.
         */
    @Override
    public JwtResponseDto login(LoginDto loginDto) {
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

        return new JwtResponseDto(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    /*
    Регистрация пользователя. Сначала идет проверка на наличие текущего пользователя в базе данных.
    Затем пользоватею устанавляиваются роли.
     */
    @Override
    public MessageResponseDto registration(RegistrationDto registrationDto) throws ObjectNotFoundException {
        if (userRepository.existsByUsername(registrationDto.getUsername()))
            return new MessageResponseDto("Error: Username is exist");

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            return new MessageResponseDto("Error: Email is exist");
        }

        User user = new User(registrationDto.getUsername(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getFirstName(), registrationDto.getLastName());

        Set<String> reqRoles = registrationDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ObjectNotFoundException(RoleExceptionConstants.NOT_FOUND_USER));
            roles.add(userRole);
        } else {
            for(String role: reqRoles){
                switch (role) {
                    case "manager":
                        Role roleManager = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new ObjectNotFoundException(RoleExceptionConstants.NOT_FOUND_MANAGER));
                        roles.add(roleManager);
                        break;

                    case "developer":
                        Role roleDeveloper = roleRepository.findByName(ERole.ROLE_DEVELOPER)
                                .orElseThrow(() -> new ObjectNotFoundException(RoleExceptionConstants.NOT_FOUND_DEVELOPER));
                        roles.add(roleDeveloper);
                        break;

                    case "tester":
                        Role modRole = roleRepository.findByName(ERole.ROLE_TESTER)
                                .orElseThrow(() -> new ObjectNotFoundException(RoleExceptionConstants.NOT_FOUND_TESTER));;
                        roles.add(modRole);
                        break;

                    default:
                        throw new ObjectNotFoundException("Error, Role " + role + " is not found");
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponseDto("User " + user.getUsername() + " CREATED");
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
