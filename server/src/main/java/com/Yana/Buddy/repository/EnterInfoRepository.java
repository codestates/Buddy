package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.EnterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnterInfoRepository extends JpaRepository<EnterInfo, Long> {

    Optional<EnterInfo> findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);

}
