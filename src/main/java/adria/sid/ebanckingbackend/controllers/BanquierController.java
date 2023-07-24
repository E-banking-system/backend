package adria.sid.ebanckingbackend.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banquier")
@Tag(name = "Banquier")
@CrossOrigin("*")
public class BanquierController {

    @GetMapping
    @PreAuthorize("hasAuthority('banquier:read')")
    public String get() {
        return "GET:: banquier controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('banquier:create')")
    @Hidden
    public String post() {
        return "POST:: banquier controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('banquier:update')")
    @Hidden
    public String put() {
        return "PUT:: banquier controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('banquier:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: banquier controller";
    }
}
