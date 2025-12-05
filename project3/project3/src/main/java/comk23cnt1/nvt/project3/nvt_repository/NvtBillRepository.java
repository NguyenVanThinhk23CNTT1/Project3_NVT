package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NvtBillRepository extends JpaRepository<NvtBill, Long> {

    boolean existsByContractIdAndBillMonthAndBillYear(Long contractId, Integer billMonth, Integer billYear);

    List<NvtBill> findByContractIdOrderByBillYearDescBillMonthDesc(Long contractId);

    List<NvtBill> findByRoomIdOrderByBillYearDescBillMonthDesc(Long roomId);
}
