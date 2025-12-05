package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.Bill;
import com.nguyenvanthinh.k23cnt1.nvtrepository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository repo;

    public List<Bill> getAll() {
        return repo.findAll();
    }

    public Bill getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void save(Bill bill) {
        repo.save(bill);
    }
}
