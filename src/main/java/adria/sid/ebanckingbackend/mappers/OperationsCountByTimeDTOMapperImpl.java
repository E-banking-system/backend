package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.operation.OperationsCountByTimeDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OperationsCountByTimeDTOMapperImpl implements OperationsCountByTimeDTOMapper{

    public OperationsCountByTimeDTO mapToDTO(Object[] queryResult) {
        String timeIntervalStart = (String) queryResult[0];
        String timeIntervalEnd = (String) queryResult[1];
        Long operationsCount = (Long) queryResult[2];
        return new OperationsCountByTimeDTO(timeIntervalStart, timeIntervalEnd, operationsCount);
    }

    public List<OperationsCountByTimeDTO> mapToDTOList(List<Object[]> queryResults) {
        return queryResults.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
