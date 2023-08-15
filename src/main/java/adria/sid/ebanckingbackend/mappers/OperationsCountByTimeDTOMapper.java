package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.operation.OperationsCountByTimeDTO;

import java.util.List;

public interface OperationsCountByTimeDTOMapper {
    OperationsCountByTimeDTO mapToDTO(Object[] queryResult);
    List<OperationsCountByTimeDTO> mapToDTOList(List<Object[]> queryResults);
}
