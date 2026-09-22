package soa.auth.service;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import soa.auth.dto.AuthResponse;
import soa.auth.dto.LoginRequest;
import soa.auth.dto.RegisterRequest;
import soa.auth.dto.UserDTO;
import soa.auth.model.User;
import soa.auth.repository.UserRepository;
import soa.auth.security.JwtUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void initDefaultUsers() {
        if (userRepository.count() == 0) {
            // Seed Admin User (Team Lead: Divishka Bypilla)
            User admin = new User(null, "Divishka Bypilla", "admin", "admin@klu.ac.in",
                    passwordEncoder.encode("admin123"), "ROLE_ADMIN");
            userRepository.save(admin);

            // Seed Researcher User (Member: Kilari Hemalatha)
            User researcher = new User(null, "Kilari Hemalatha", "hemalatha", "hemalatha@klu.ac.in",
                    passwordEncoder.encode("hema123"), "ROLE_RESEARCHER");
            userRepository.save(researcher);

            // Seed Standard Student User (Member: Challagunda Nikhitha)
            User student = new User(null, "Challagunda Nikhitha", "nikhitha", "nikhitha@klu.ac.in",
                    passwordEncoder.encode("nikki123"), "ROLE_USER");
            userRepository.save(student);
        }
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email address is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already registered");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already registered");
        }

        String role = request.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER";
        } else if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }

        User user = new User();
        user.setName(request.getName() != null ? request.getName() : request.getUsername());
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                "User registered successfully"
        );
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            // Also try email login
            userOpt = userRepository.findByEmail(request.getUsername());
        }

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
        );
    }

    public Map<String, Object> validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !jwtUtil.validateToken(token)) {
            return Map.of("valid", false, "message", "Invalid or expired token");
        }

        var claims = jwtUtil.extractAllClaims(token);
        return Map.of(
                "valid", true,
                "userId", claims.get("userId"),
                "username", claims.getSubject(),
                "role", claims.get("role"),
                "email", claims.get("email"),
                "name", claims.get("name")
        );
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long userId) {
        return userRepository.findById(userId).map(UserDTO::new);
    }
}
