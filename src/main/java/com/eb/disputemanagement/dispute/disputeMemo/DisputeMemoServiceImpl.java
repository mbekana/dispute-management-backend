package com.eb.disputemanagement.dispute.disputeMemo;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestRepository;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestService;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;

import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.eb.disputemanagement.dispute.utils.Util.getEmployeeID;
import static com.eb.disputemanagement.dispute.utils.Util.getNullPropertyNames;


@Service
@RequiredArgsConstructor
public class DisputeMemoServiceImpl implements DisputeMemoService {
    private final DisputeRequestService disputeRequestService;
    private final DisputeMemoRepository disputeMemoRepository;
    private final DisputeRequestRepository disputeRequestRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeClient employeeClient;

    @Override
    public DisputeMemo createDisputeMemo(long id, JwtAuthenticationToken token, DisputeMemo disputeMemo) throws IllegalAccessException {
        var dr = disputeRequestService.getDisputeRequest(id);
        var employeeID = getEmployeeID(token);
        var employee = getEmployee(employeeID);
        dr.setPayerBranch(disputeMemo.getPayerBranch());
        dr.setPayerBranchName(disputeMemo.getPayerBranchName());
        disputeMemo.setEmployee(employee);
        disputeMemo.setDisputeRequest(dr);
        dr.setDisputeMemo(disputeMemo);
        return  disputeMemoRepository.save(disputeMemo);
    }

    @Override
    public DisputeMemo getDisputeMemo(long id) {
        var dm = disputeMemoRepository.findById(id).orElseThrow(() -> new OpenApiResourceNotFoundException("Dispute Memo with an Id " + id + " was not found"));
        return dm;
    }

    @Override
    public Page<DisputeMemo> getDisputeMemos(Pageable pageable) {
        return disputeMemoRepository.findAll(pageable);
    }

    @Override
    public void deleteDisputeMemo(long id) {
        disputeMemoRepository.deleteById(id);
    }

    @Override
    public DisputeMemo updateDisputeMemo(long id, DisputeMemo disputeMemo, JwtAuthenticationToken token) throws IllegalAccessException {
        var dm = getDisputeMemo(id);
        var dr= dm.getDisputeRequest();
//        dm.setPayerBranch(disputeMemo.getPayerBranch());
//        dm.setCashOnATM(disputeMemo.getCashOnATM());
//        dm.setFailedMachineName(disputeMemo.getFailedMachineName());
//        dm.setAtmCashWithdrawalFee(disputeMemo.getAtmCashWithdrawalFee());
//        dm.setNetworkSuspenseGl2(disputeMemo.getNetworkSuspenseGl2());
//        dm.setNetworkSuspenseGl(disputeMemo.getNetworkSuspenseGl());
//        dm.setNetworkSuspenseGl2(disputeMemo.getNetworkSuspenseGl2());
//        dm.setId(id);
        BeanUtils.copyProperties(disputeMemo, dm, getNullPropertyNames(disputeMemo));
        dr.setPayerBranchName(disputeMemo.getPayerBranchName());
        dr.setPayerBranch(disputeMemo.getPayerBranch());
        return disputeMemoRepository.save(dm);
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }

}
