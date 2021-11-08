package com.Yana.Buddy.service;

import com.Yana.Buddy.entity.EnterInfo;
import com.Yana.Buddy.repository.EnterInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EnterInfoService {

    private final EnterInfoRepository enterInfoRepository;

    public void save(EnterInfo info) {
        enterInfoRepository.save(info);
    }

    public Optional<EnterInfo> findBySessionId(String sessionId) {
        return enterInfoRepository.findBySessionId(sessionId);
    }

    public void deleteBySessionId(String sessionId) {
        enterInfoRepository.deleteBySessionId(sessionId);
    }

}
