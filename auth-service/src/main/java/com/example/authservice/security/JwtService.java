package com.example.authservice.security;

import com.example.authservice.model.CuentaAcceso;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JwtService {
    private static final String SECRET = "eSportsArenaManagerAuthServiceSecret256bits";

    public String emitir(CuentaAcceso cuenta) {
        String header = base64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        long exp = System.currentTimeMillis() / 1000 + 86400;
        String payload = base64("{\"sub\":\"" + cuenta.getEmail() + "\",\"id\":" + cuenta.getId()
                + ",\"rol\":\"" + cuenta.getRol() + "\",\"estado\":\"" + cuenta.getEstado() + "\",\"exp\":" + exp + "}");
        return header + "." + payload + "." + firmar(header + "." + payload);
    }

    public boolean validar(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3 && firmar(parts[0] + "." + parts[1]).equals(parts[2]);
    }

    private static String base64(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String firmar(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible firmar el token", e);
        }
    }
}
