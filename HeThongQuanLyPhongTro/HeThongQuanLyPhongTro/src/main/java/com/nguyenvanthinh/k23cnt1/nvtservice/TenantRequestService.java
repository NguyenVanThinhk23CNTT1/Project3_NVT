package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.TenantRequest;
import com.nguyenvanthinh.k23cnt1.nvtrepository.TenantRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantRequestService {

    private final TenantRequestRepository repo;

    public List<TenantRequest> getAll() {
        return repo.findAll();
    }

    public TenantRequest getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(TenantRequest req) {
        repo.save(req);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public void approve(Long id) {
    }

    public void reject(Long id, String reason) {

    }
}
