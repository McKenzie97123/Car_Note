package Service;

import android.widget.Toast;

import java.security.MessageDigest;

public class PasswordHasher {
    public String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            Toast.makeText(null, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
