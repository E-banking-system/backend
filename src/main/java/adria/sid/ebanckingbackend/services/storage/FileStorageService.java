package adria.sid.ebanckingbackend.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void storeFile(byte[] content, String fileName);
    String storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);
}
