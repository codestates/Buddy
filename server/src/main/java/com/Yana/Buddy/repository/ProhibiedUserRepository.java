package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.ProhibiedUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProhibiedUserRepository extends JpaRepository<ProhibiedUserEntity, Long> {
}
