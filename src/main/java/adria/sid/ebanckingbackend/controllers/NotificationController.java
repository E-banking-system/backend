package adria.sid.ebanckingbackend.controllers;

import adria.sid.ebanckingbackend.dtos.notification.NotificationResDTO;
import adria.sid.ebanckingbackend.entities.Notification;
import adria.sid.ebanckingbackend.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@CrossOrigin("*")
public class NotificationController {
    final private NotificationService notificationService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('banquier_client:get_notifications_by_user_id')")
    public ResponseEntity<Page<NotificationResDTO>> getNotificationByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam String userId
    ) {
        // Create a Pageable object to represent the pagination information
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        // Retrieve the paginated accounts data from the service
        Page<NotificationResDTO> notificationPage = notificationService.getNotificationsByUserId(pageable,userId);

        return ResponseEntity.ok(notificationPage);
    }
}
