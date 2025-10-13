package org.Client.Services;

import java.security.SecureRandom;
import java.util.Base64;

public class ServiceFunctions {
    private static final SecureRandom secureRandom = new SecureRandom(); // thread-safe
    private static final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();

    public static String GenerateSessionToken(){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64UrlEncoder.encodeToString(bytes);
    }
}
