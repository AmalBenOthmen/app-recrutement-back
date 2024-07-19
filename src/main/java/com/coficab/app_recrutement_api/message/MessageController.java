package com.coficab.app_recrutement_api.message;

    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;
    import java.util.List;
@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('USER')")
    public Message sendMessage(@RequestBody Message message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        message.setSentDate(LocalDateTime.now());
        return messageService.saveMessage(message);
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Message> getUnreadMessages() {
        return messageService.getUnreadMessages();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PutMapping("/read/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Message markAsRead(@PathVariable Integer id) {
        return messageService.markAsRead(id);
    }
}
