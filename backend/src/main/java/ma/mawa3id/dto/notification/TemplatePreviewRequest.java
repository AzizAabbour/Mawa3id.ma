package ma.mawa3id.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplatePreviewRequest {
    private String templateBody;
    private String sampleCustomerName;
    private String sampleServiceName;
    private String sampleDate;
    private String sampleTime;
    private String sampleBusinessName;
    private String sampleBusinessAddress;
}
