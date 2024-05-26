package com.example.onetimemessage.onetimemessage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@ToString(exclude = "meeting")
public class CityEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column
    private String cityFullName;

    @Column
    private double lat;

    @Column
    private double lon;

    @OneToOne(mappedBy = "city")
    private  MeetingEntity meeting;
}
