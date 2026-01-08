package com.unieus.garajea.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SecurityUtil {
    
    private SecurityUtil() {
        // Eraikitzaile pribatua instantziak saihesteko
    }
    
    public static String hashPasahitza(String pasahitza) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pasahitza.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errorea pasahitza kodetzean (Hashing)", e);
        }
    }
    
    // Beste segurtasun metodo batzuk
    public static boolean verifyPasahitza(String pasahitza, String hashedPasahitza) {
        return hashPasahitza(pasahitza).equals(hashedPasahitza);
    }
}
