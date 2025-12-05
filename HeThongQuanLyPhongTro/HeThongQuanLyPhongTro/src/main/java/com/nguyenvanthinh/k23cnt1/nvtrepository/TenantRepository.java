package com.nguyenvanthinh.k23cnt1.nvtrepository;

import com.nguyenvanthinh.k23cnt1.nvtentity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {}
