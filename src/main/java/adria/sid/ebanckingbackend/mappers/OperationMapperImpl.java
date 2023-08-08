package adria.sid.ebanckingbackend.mappers;

import adria.sid.ebanckingbackend.dtos.operation.OperationResDTO;
import adria.sid.ebanckingbackend.entities.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OperationMapperImpl implements OperationMapper{
    @Override
    public OperationResDTO fromOperationToOperationResDTO(Operation operation) {
        OperationResDTO operationResDTO=new OperationResDTO();
        BeanUtils.copyProperties(operation,operationResDTO);
        return operationResDTO;
    }
}
