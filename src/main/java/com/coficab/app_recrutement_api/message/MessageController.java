package com.coficab.app_recrutement_api.message;

    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;
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
            // Optionally, you can set the sender or perform additional checks with currentPrincipalName
            return messageService.saveMessage(message);
        }

        @GetMapping("/unread")
        @PreAuthorize("hasRole('ADMIN')")
        public List<Message> getUnreadMessages() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            // Optionally, perform checks or logging with currentPrincipalName

            return messageService.getUnreadMessages();
        }
    }


