package com.ga.tms.security;

import com.ga.tms.auth.model.LoginRequest;
import com.ga.tms.auth.model.LoginResponse;
import com.ga.tms.exceptions.InformationExistException;
import com.ga.tms.exceptions.InformationNotFoundException;
import com.ga.tms.exceptions.InvalidTokenException;
import com.ga.tms.model.PasswordResetToken;
import com.ga.tms.model.User;
import com.ga.tms.model.VerificationToken;
import com.ga.tms.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;
    private final EmailService emailService;

    @Value("${server.port}")
    private String portNumber;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetails myUserDetails,
                       @Lazy VerificationTokenRepository verificationTokenRepository,
                       @Lazy PasswordResetTokenRepository passwordResetTokenRepository,
                       @Lazy EmailService emailService
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @Transactional(rollbackOn = Exception.class)
    public User createUser(User userObject){
        if (!userRepository.existsByUsername(userObject.getUsername())){
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            User user = userRepository.save(userObject);
            user.setActive(false);

            VerificationToken vt = new VerificationToken();
            vt.setToken(UUID.randomUUID().toString());
            vt.setUser(userObject);
            vt.setExpiryDate(LocalDateTime.now().plusHours(24));
            verificationTokenRepository.save(vt);
            emailService.sendVerificationEmail(userObject.getEmail(), generateVerificationLink(vt.getToken()));

            return user;
        } else {
            throw new InformationExistException("user with username " + userObject.getUsername()  + " already exists.");
        }
    }

    public boolean verify(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (verificationToken.isUsed()){
            throw new InvalidTokenException("Token is already used");
        }
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new InvalidTokenException("Token is expired");
        }

        User user = verificationToken.getUser();
        user.setActive(true);
        verificationToken.setUsed(true);

        userRepository.save(user);
        verificationTokenRepository.save(verificationToken);

        return true;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            var authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
            if (!myUserDetails.getActive()){
                return ResponseEntity.ok(new LoginResponse("The account is not active"));
            }
            final String jwt = jwtUtils.generateJwtToken(myUserDetails);
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.ok(new LoginResponse("Error : user name or password is incorrect"));
        }
    }

    public String generateVerificationLink(String token){
        return "http://localhost:" + portNumber + "/auth/users/verify/" + token;
    }

    public String generatePasswordResetLink(String token) {
        return "http://localhost:" + portNumber + "/auth/users/reset-password?token=" + token;
    }

    @Transactional(rollbackOn = Exception.class)
    public void forgotPassword(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return;
        }
        passwordResetTokenRepository.findByUser_Id(user.getId())
                .forEach(passwordResetTokenRepository::delete);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(UUID.randomUUID().toString());
        prt.setUser(user);
        prt.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(prt);

        emailService.sendPasswordResetEmail(user.getEmail(), generatePasswordResetLink(prt.getToken()));
    }

    @Transactional(rollbackOn = Exception.class)
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (prt.isUsed()) {
            throw new InvalidTokenException("Token is already used");
        }
        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token is expired");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        prt.setUsed(true);

        userRepository.save(user);
        passwordResetTokenRepository.save(prt);
    }

    @Transactional(rollbackOn = Exception.class)
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    public User updateProfile(Long userId, String fullName, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        if (fullName != null) {
            user.setFullName(fullName);
        }
        if (email != null) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    public void uploadProfilePicture(Long userId, byte[] picture) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        user.setProfilePicture(picture);
        userRepository.save(user);
    }

    public byte[] getProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InformationNotFoundException("User not found"));

        return user.getProfilePicture();
    }
}
