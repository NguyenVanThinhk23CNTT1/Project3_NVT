package comk23cnt1.nvt.project3.nvt_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NvtContractMemberId implements Serializable {
    private Long contractId;
    private Long tenantId;
}
