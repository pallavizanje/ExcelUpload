import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DQFrameworkUpdateResponse {
    private boolean success;
    private String message;
    private Object data;
    private List<String> errors;
}
