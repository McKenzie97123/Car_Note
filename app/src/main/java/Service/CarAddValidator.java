package Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarAddValidator {
    public boolean plateNumberValidate(String plateNumber) {
        String regex = "^[A-Za-z][A-Za-z0-9]{0,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(plateNumber);
        return matcher.matches();
    }
}
