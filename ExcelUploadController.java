import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/uploadRCExcel")
@RequiredArgsConstructor
public class ExcelUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUploadController.class);

    private final ExcelUploadService service;

    @PostMapping(value = "/{targetSystemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DQFrameworkUpdateResponse> handleFormUpload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("targetSystemId") Integer targetSystemId
    ) {
        try {
            LOGGER.info("UploadExcel called with targetSystemId: {}", targetSystemId);

            // Call service method (it should throw DQFrameworkServiceException if validation fails)
            service.headExcel(file, targetSystemId);

            return ResponseEntity.ok(
                    DQFrameworkUpdateResponse.builder()
                            .success(true)
                            .message("Data created/updated successfully")
                            .build()
            );

        } catch (DQFrameworkServiceException def) {
            LOGGER.error("Validation error occurred: ", def);
            return ResponseEntity
                    .badRequest()
                    .body(DQFrameworkUpdateResponse.builder()
                            .success(false)
                            .message("Validation failed")
                            .errors(Collections.singletonList(def.getMessage()))
                            .build());

        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DQFrameworkUpdateResponse.builder()
                            .success(false)
                            .message("Unexpected error: " + e.getMessage())
                            .build());
        }
    }
}
