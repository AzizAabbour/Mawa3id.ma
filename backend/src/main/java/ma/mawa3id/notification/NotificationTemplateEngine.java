package ma.mawa3id.notification;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Template engine replacing placeholders:
 * {{customerName}}, {{businessName}}, {{serviceName}}, {{appointmentDate}},
 * {{appointmentTime}}, {{businessPhone}}, {{businessAddress}}
 */
@Component
public class NotificationTemplateEngine {

    private static final DateTimeFormatter DATE_FORMATTER_FR =
            DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public String render(String templateBody, NotificationMessage message) {
        if (templateBody == null) return "";

        Map<String, String> values = new HashMap<>();
        values.put("customerName", message.getCustomerName() != null ? message.getCustomerName() : "Client");
        values.put("businessName", message.getBusinessName() != null ? message.getBusinessName() : "Notre établissement");
        values.put("serviceName", message.getServiceName() != null ? message.getServiceName() : "Prestation");
        values.put("appointmentDate", message.getAppointmentDate() != null ? message.getAppointmentDate().format(DATE_FORMATTER_FR) : "");
        values.put("appointmentTime", message.getAppointmentTime() != null ? message.getAppointmentTime().format(TIME_FORMATTER) : "");
        values.put("businessAddress", message.getBusinessAddress() != null ? message.getBusinessAddress() : "");
        values.put("businessPhone", message.getBusinessPhone() != null ? message.getBusinessPhone() : "");

        String result = templateBody;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    public String renderPreview(String templateBody, Map<String, String> sampleValues) {
        if (templateBody == null) return "";
        String result = templateBody;
        for (Map.Entry<String, String> entry : sampleValues.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
