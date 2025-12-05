package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.Contract;
import com.nguyenvanthinh.k23cnt1.nvtrepository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository repo;

    public List<Contract> getAll() {
        return repo.findAll();
    }

    public Contract getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(Contract contract) {
        repo.save(contract);
    }
}
