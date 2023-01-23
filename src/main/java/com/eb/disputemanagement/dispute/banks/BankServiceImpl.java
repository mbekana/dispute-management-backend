package com.eb.disputemanagement.dispute.banks;


import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.eb.disputemanagement.dispute.utils.Util;
import com.eb.disputemanagement.dispute.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.Specification.where;


@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final EmployeeMapper employeeMapper;

    private final EmployeeClient employeeClient;

    @Override
    public Page<Bank> getBanks(Pageable pageable) {
        return bankRepository.findAll(pageable);
    }
    @Override
    public Bank createBank(Bank bank) throws IllegalAccessException {
        if(existsBySwiftCode(bank.getSwiftCode())){
            throw new IllegalAccessException("Bank with name " + bank.getName() + " already exists!");
        }
        return bankRepository.save(bank);
    }
    @Override
    public Bank updateBank(long id, Bank bank) {
        var b = bankRepository.findById(id).orElseThrow(()->
            new OpenApiResourceNotFoundException("Bank Not found with id " + id )
        );
        BeanUtils.copyProperties(bank, b, Util.getNullPropertyNames(bank));
        return bankRepository.save(b);
    }
    @Override
    public void deleteBank(long id, JwtAuthenticationToken token) {
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        var b = bankRepository.findById(id).orElseThrow(()-> new OpenApiResourceNotFoundException("Bank Not found with Id " + id));
        b.setDeletedBy(employee.getFullName());
        b.setDeletedAt(LocalDateTime.now());
        bankRepository.save(b);
        bankRepository.deleteById(id);
    }
    @Override
    public Bank getBank(long id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Bank.class, "Id", String.valueOf(id)));
    }

    @Override
    public boolean existsBySwiftCode(String swiftCode) {
        return bankRepository.existsBySwiftCode(swiftCode);
    }

    @Override
    public Bank getBySwiftCode(String swiftCode) {
        return bankRepository.findBySwiftCode(swiftCode);
    }

    @Override
    public Page<Bank> searchBank(Specification<Bank> search, Pageable pageable) {
       return bankRepository.findAll(where(search), pageable);
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }
}
