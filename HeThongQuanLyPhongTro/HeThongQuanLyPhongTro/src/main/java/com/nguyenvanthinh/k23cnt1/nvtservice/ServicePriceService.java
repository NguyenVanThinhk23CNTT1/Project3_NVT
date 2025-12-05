package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.ServicePrice;
import com.nguyenvanthinh.k23cnt1.nvtrepository.ServicePriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePriceService {

    private final ServicePriceRepository repo;

    public List<ServicePrice> getAll() {
        return repo.findAll();
    }

    public void save(ServicePrice sp) {
        repo.save(sp);
    }
}
