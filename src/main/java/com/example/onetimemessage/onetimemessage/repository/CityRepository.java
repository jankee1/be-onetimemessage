package com.example.onetimemessage.onetimemessage.repository;

import com.example.onetimemessage.onetimemessage.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, UUID> {
}
