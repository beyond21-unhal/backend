package com.team3.statsservice.service;

import com.team3.statsservice.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
}
