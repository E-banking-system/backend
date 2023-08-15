package adria.sid.ebanckingbackend.dtos.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationsCountByTimeDTO {
    private String timeIntervalStart;
    private String timeIntervalEnd;
    private Long operationsCount;

}
