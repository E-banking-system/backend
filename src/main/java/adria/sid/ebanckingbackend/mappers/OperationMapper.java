package adria.sid.ebanckingbackend.mappers;


import adria.sid.ebanckingbackend.dtos.operation.OperationResDTO;
import adria.sid.ebanckingbackend.entities.Operation;

public interface OperationMapper {
    OperationResDTO fromOperationToOperationResDTO(Operation operation);
}
