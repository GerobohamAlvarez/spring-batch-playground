package com.springbatch.springbatch.mappers;

import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.stereotype.Service;

import com.springbatch.springbatch.dto.PolicyDTO;

@Service
public class PolicyExcelRowMapper implements RowMapper<PolicyDTO> {

    @Override
    public PolicyDTO mapRow(RowSet rs) throws Exception {
        PolicyDTO policyDTO = new PolicyDTO();

        if (rs == null || rs.getCurrentRow() == null) {
            return null;
        }

        //Arrays.asList(rs.getCurrentRow()).stream().forEach(rowElemen -> System.out.println(rowElemen));

        System.out.println("Reading row : " + rs.getCurrentRowIndex());
        policyDTO.setPolicy(Long.parseLong(rs.getCurrentRow()[0]));

        return policyDTO;
    }
}