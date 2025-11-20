package com.nvtdevmaster.lesson08.service;

import com.nvtdevmaster.lesson08.entity.Configuration;
import com.nvtdevmaster.lesson08.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public Configuration findById(Long id) {
        return configurationRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        configurationRepository.deleteById(id);
    }
}
