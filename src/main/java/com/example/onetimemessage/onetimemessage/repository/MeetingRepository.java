package com.example.onetimemessage.onetimemessage.repository;

import com.example.onetimemessage.onetimemessage.entity.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID> {
}