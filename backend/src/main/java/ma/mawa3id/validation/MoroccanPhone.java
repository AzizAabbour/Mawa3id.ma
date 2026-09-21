package ma.mawa3id.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates Moroccan phone number formats:
 * - Local: 05XXXXXXXX, 06XXXXXXXX, 07XXXXXXXX (10 digits)
 * - International: +2125XXXXXXXX, +2126XXXXXXXX, +2127XXXXXXXX
 * - International without +: 2125XXXXXXXX, 2126XXXXXXXX, 2127XXXXXXXX
 */
@Documented
@Constraint(validatedBy = MoroccanPhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MoroccanPhone {
    String message() default "Format de numéro de téléphone marocain invalide (ex: +212612345678 ou 0612345678)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
