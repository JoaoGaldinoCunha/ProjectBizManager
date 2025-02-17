package com.bizmanager.inventory.service;

import com.bizmanager.inventory.model.request.LoginRequest;
import com.bizmanager.inventory.model.response.LoginResponse;
import com.bizmanager.inventory.repository.CompanyRepository;
import com.bizmanager.inventory.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponse> authenticationLogin(LoginRequest loginRequest) {

        var employee = employeesRepository.findByEmail(loginRequest.email());
        var company = companyRepository.findByEmail(loginRequest.email());

        if (employee.isEmpty() && company.isEmpty()) {
            throw new BadCredentialsException("Email ou senha são inválidos.");
        }

        boolean isEmployeeLoginCorrect = employee.isPresent() && employee.get().isLoginCorrect(loginRequest, passwordEncoder);
        boolean isCompanyLoginCorrect = company.isPresent() && company.get().isLoginCorrect(loginRequest, passwordEncoder);

        if (!isEmployeeLoginCorrect && !isCompanyLoginCorrect) {
            throw new BadCredentialsException("Email ou senha são inválidos.");
        }

        var now = Instant.now();
        var expiresIn = 2700L;

        String roleName;
        if (employee.isPresent() && isEmployeeLoginCorrect) {
            roleName = employee.get().getRole().getName().toString();
        } else {
            roleName = company.get().getRole().getName().toString();
        }
        var claims = JwtClaimsSet.builder()
                .issuer("DataBaseBizManager")
                .subject(employee.isPresent()
                        ? employee.get().getId().toString() + "-" + employee.get().getCompany().getId().toString() + "-" + employee.get().getRole().getName().toString()
                        : company.get().getId().toString() + "-" + company.get().getRole().getName().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", roleName)
                .build();

        System.out.println(claims.getClaims());

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
