package ma.mawa3id.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class MoroccanPhoneValidator implements ConstraintValidator<MoroccanPhone, String> {

    // Matches: 05XXXXXXXX, 06XXXXXXXX, 07XXXXXXXX, +2125XXXXXXXX, +2126XXXXXXXX, +2127XXXXXXXX, 2125XXXXXXXX, 2126XXXXXXXX, 2127XXXXXXXX
    private static final Pattern MOROCCAN_PHONE_PATTERN =
            Pattern.compile("^(?:\\+212|212|0)([5-7]\\d{8})$");

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return true; // Use @NotBlank / @NotNull for mandatory checks
        }
        return MOROCCAN_PHONE_PATTERN.matcher(phone.trim().replaceAll("\\s+", "")).matches();
    }

    /**
     * Helper to normalize Moroccan phone numbers to international standard +212XXXXXXXXX
     */
    public static String normalize(String phone) {
        if (phone == null) return null;
        String clean = phone.trim().replaceAll("[\\s\\-\\.]", "");
        if (clean.startsWith("+212")) {
            return clean;
        } else if (clean.startsWith("212")) {
            return "+" + clean;
        } else if (clean.startsWith("0") && clean.length() == 10) {
            return "+212" + clean.substring(1);
        }
        return clean;
    }
}
