package com.example.onetimemessage.onetimemessage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = "message")
public class MeetingEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;

    @Column
    private Double minTemp;

    @Column
    private Double maxTemp;

    @Column(nullable = false)
    private LocalDateTime date;

    @OneToOne(mappedBy = "meeting")
    private MessageEntity message;
}
