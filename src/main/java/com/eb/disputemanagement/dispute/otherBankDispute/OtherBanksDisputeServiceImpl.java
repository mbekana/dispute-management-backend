package com.eb.disputemanagement.dispute.otherBankDispute;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.utils.Util;
import com.eb.disputemanagement.dispute.common.DRequestType;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.eb.disputemanagement.dispute.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class OtherBanksDisputeServiceImpl implements OtherBanksDisputeService{

    private final OtherBanksDisputeRepository otherBanksDisputeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeClient employeeClient;

    @Override
    public Page<OtherBanksDispute> getOtherBanksDisputes(Pageable pageable) {
        return otherBanksDisputeRepository.findAll(pageable);
    }

    @Override
    public OtherBanksDispute createOtherBanksDispute(JwtAuthenticationToken token, OtherBanksDispute otherBanksDispute) {
        otherBanksDispute.setEmployee(getEmployee(Util.getEmployeeID(token)));
        otherBanksDispute.setRequestType(DRequestType.OTHER_BANK_ON_ENAT);
        return otherBanksDisputeRepository.save(otherBanksDispute);
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }
    @Override
    public OtherBanksDispute updateOtherBanksDispute(long id, JwtAuthenticationToken token, OtherBanksDispute otherBanksDispute) throws IllegalAccessException {
        var employeeId = Util.getEmployeeID(token);
        var obdr = getOtherBanksDispute(id);
        BeanUtils.copyProperties(otherBanksDispute, obdr, Util.getNullPropertyNames(otherBanksDispute));
        if (!obdr.getEmployee().getEmployeeId().equals(employeeId)) {
            throw new IllegalAccessException("You can not update this request");
        }
        return otherBanksDisputeRepository.save(obdr);
    }

    @Override
    public void deleteOtherBanksDispute(long id) {
        otherBanksDisputeRepository.deleteById(id);
    }

    @Override
    public OtherBanksDispute getOtherBanksDispute(long id) {
        return otherBanksDisputeRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(OtherBanksDispute.class, "Id", String.valueOf(id)));
    }

    @Override
    public Page<OtherBanksDispute> searchOtherBankDispute(Specification<OtherBanksDispute> search, Pageable pageable) {
        return otherBanksDisputeRepository.findAll(where(search), pageable);
    }
}
