package com.nguyenvanthinh.k23cnt1.nvtservice;

import com.nguyenvanthinh.k23cnt1.nvtentity.PaymentInfo;
import com.nguyenvanthinh.k23cnt1.nvtrepository.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentInfoService {

    private final PaymentInfoRepository repo;

    public PaymentInfo get() {
        return repo.findAll().isEmpty() ? null : repo.findAll().get(0);
    }

    public void save(PaymentInfo info) {
        repo.save(info);
    }
}
