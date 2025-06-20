package com.progaming.tutorial.service.impl;

import com.progaming.tutorial.config.AuthProperties;
import com.progaming.tutorial.dto.RequestCreateUserDto;
import com.progaming.tutorial.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAdminServiceImpl implements KeycloakAdminService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthProperties authProperties;

    public String getAdminAccessToken() {
        String tokenUrl = authProperties.getServerUrl() + "/realms/" + authProperties.getRealm() + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", authProperties.getClientId());
        body.add("username", authProperties.getUsername());
        body.add("password", authProperties.getPassword());

        HttpEntity<?> req = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(tokenUrl, req, Map.class);

        return (String) response.get("access_token");
    }

    @Override
    public void createUserAndAssignRole(RequestCreateUserDto request) {
        String token = getAdminAccessToken();

        // 1. Tạo user
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", request.getUsername());
        userPayload.put("email", request.getEmail());
        userPayload.put("enabled", true);
        Map<String, Object> credentials = Map.of(
                "type", "password",
                "value", request.getPassword(),
                "temporary", false
        );
        userPayload.put("credentials", List.of(credentials));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(userPayload, headers);
        restTemplate.postForEntity(authProperties.getServerUrl() + "/admin/realms/" + authProperties.getRealm() + "/users", entity, String.class);

        // 2. Lấy ID user
        String userId = findUserIdByUsername(request.getUsername());
        assignRoleToUser(userId, "user");
    }

    public String findUserIdByUsername(String username) {
        String token = getAdminAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        String url = authProperties.getServerUrl() + "/admin/realms/" + authProperties.getRealm() + "/users?username=" + username;
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), List.class);
        Map user = (Map) response.getBody().get(0);
        return (String) user.get("id");
    }

    public void assignRoleToUser(String userId, String roleName) {
        String token = getAdminAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String roleUrl = authProperties.getServerUrl() + "/admin/realms/" + authProperties.getRealm() + "/roles/" + roleName;
        Map role = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();

        String assignUrl = authProperties.getServerUrl() + "/admin/realms/" + authProperties.getRealm() + "/users/" + userId + "/role-mappings/realm";
        restTemplate.postForEntity(assignUrl, new HttpEntity<>(List.of(role), headers), String.class);
    }
}
