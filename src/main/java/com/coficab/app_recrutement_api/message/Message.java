package com.coficab.app_recrutement_api.message;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String email;

    @Column(length = 500)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private boolean isRead;


    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;


}
