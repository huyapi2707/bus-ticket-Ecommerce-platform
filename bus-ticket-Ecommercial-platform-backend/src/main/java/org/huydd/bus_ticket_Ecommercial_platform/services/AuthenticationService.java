package org.huydd.bus_ticket_Ecommercial_platform.services;

import lombok.RequiredArgsConstructor;
import org.huydd.bus_ticket_Ecommercial_platform.dtos.UserDTO;
import org.huydd.bus_ticket_Ecommercial_platform.exceptions.AccessDeniedException;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.Role;
import org.huydd.bus_ticket_Ecommercial_platform.pojo.User;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.AuthenticationRequest;
import org.huydd.bus_ticket_Ecommercial_platform.requestModels.RegisterRequest;
import org.huydd.bus_ticket_Ecommercial_platform.responseModels.AuthenticationResponse;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final GoogleOauth2Service googleOauth2Service;

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final MailSenderService mailSenderService;

    private final JwtService jwtService;

    public AuthenticationResponse authenticateUser(AuthenticationRequest payload) {
        User user = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        payload.getUsername(),
                        payload.getPassword()
                )
        ).getPrincipal();
        if (!user.getIsActive()) throw new AccessDeniedException("Your account is inactive");
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .userDetails(userService.toDTO(user))
                .build();
    }

    public AuthenticationResponse registerUser(RegisterRequest payload) {
        User user = userService.getUserByUsername(payload.getUsername());

        if (user != null) {
            throw new IllegalArgumentException("Username  is exist");
        }
        if (userService.isEmailExist(payload.getEmail())) {
            throw new IllegalArgumentException("Email is exist");
        }
        Role role = roleService.getRoleByName(payload.getRole());
        if (role == null) {
            throw new IllegalArgumentException("Role field is invalid");
        }
        User newUser = User.builder()
                .email(payload.getEmail())
                .username(payload.getUsername())
                .password(passwordEncoder.encode(payload.getPassword()))
                .role(role)
                .isActive(true)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        userService.saveUser(newUser);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(newUser))
                .userDetails(userService.toDTO(newUser))
                .build();
    }


    private String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "-1";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }


    public String createLoginUrl (String authorizationServer, String state) {
        switch (authorizationServer) {
            case "google":
            {
                return googleOauth2Service.createAuthorizationUrl(state);
            }
            default: return null;
        }
    }

    public AuthenticationResponse verifyOauth2Request(String authorizationServer, String code) throws GeneralSecurityException, IOException {
        UserDTO userInfo = null;
        switch (authorizationServer) {
            case "google":
            {
                userInfo = googleOauth2Service.exchangeAuthorizationCode(code);
            }
        }
        if (userInfo == null) {
            throw new GeneralSecurityException("Invalid code");
        }

        User user = userService.findByEmail(userInfo.getEmail());

        if (user == null) {
            Role role = roleService.getRoleByName("CUSTOMER");
            String password = passwordEncoder.encode(generatePassword());
            user = User.builder()
                    .username(userInfo.getUsername())
                    .email(userInfo.getEmail())
                    .firstName(userInfo.getFirstname())
                    .username(userInfo.getEmail())
                    .lastName(userInfo.getLastname())
                    .avatar(userInfo.getAvatar())
                    .role(role)
                    .isActive(true)
                    .password(password)
                    .build();
            userService.saveUser(user);
            mailSenderService.sendEmail(
                    user.getEmail(),
                    "Đăng ký tài khoản tại Bus Station",
                    String.format("Mật khẩu tài khoản của bạn là %s", password));
        }
        if (!user.getIsActive()) throw new AccessDeniedException("Your account is inactive");
        String accessToken = jwtService.generateToken(user);
        UserDTO userDTO = userService.toDTO(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .userDetails(userDTO)
                .build();
    }
}
