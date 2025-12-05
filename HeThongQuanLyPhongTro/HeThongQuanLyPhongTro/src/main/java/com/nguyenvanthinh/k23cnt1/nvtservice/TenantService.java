package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.Tenant;
import com.nguyenvanthinh.k23cnt1.nvtrepository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repo;

    public List<Tenant> getAll() {
        return repo.findAll();
    }

    public Tenant getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(Tenant tenant) {
        repo.save(tenant);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
