package ma.mawa3id.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MoroccanPhoneValidatorTest {

    private MoroccanPhoneValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MoroccanPhoneValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0612345678",
            "0777996998",
            "0522123456",
            "+212612345678",
            "+212777996998",
            "+212522123456",
            "212612345678",
            "+212 6 12 34 56 78"
    })
    void testValidMoroccanPhoneNumbers(String phone) {
        assertTrue(validator.isValid(phone, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0123456789",    // 01 not a Moroccan prefix
            "061234567",     // Too short
            "061234567890",   // Too long
            "invalid_phone",
            "+33612345678"   // France prefix
    })
    void testInvalidMoroccanPhoneNumbers(String phone) {
        assertFalse(validator.isValid(phone, null));
    }

    @Test
    void testNormalization() {
        assertEquals("+212612345678", MoroccanPhoneValidator.normalize("0612345678"));
        assertEquals("+212777996998", MoroccanPhoneValidator.normalize("+212777996998"));
        assertEquals("+212612345678", MoroccanPhoneValidator.normalize("212612345678"));
        assertEquals("+212612345678", MoroccanPhoneValidator.normalize("06 12 34 56 78"));
    }
}
