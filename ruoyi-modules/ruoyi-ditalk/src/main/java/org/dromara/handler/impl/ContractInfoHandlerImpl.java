package org.dromara.handler.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.enums.ContractStateEnum;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.handler.IContractInfoHandler;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.service.IContractInfoService;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

/**
 * 合同信息应用接口
 *
 * @author weidixian
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractInfoHandlerImpl implements IContractInfoHandler {

    private final IContractInfoService contractInfoService;
    private final ICustomerInfoService customerInfoService;

    /**
     * 新增合同信息，当前用户和部门自动赋值，状态默认为草稿。
     *
     * @param bo 合同信息业务对象
     * @return 是否新增成功
     */
    @Override
    @DSTransactional
    public Boolean add(ContractInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(bo.getCustomerId()); // 线索不可以创建合同
        if (customerInfoVo == null) {
            throw new UserException("客户不存在，无法创建合同");
        }
        bo.setAssignedTo(LoginHelper.getUserId());
        bo.setAssignedDept(LoginHelper.getDeptId());
        bo.setState(ContractStateEnum.DRAFT.getCode());
        return contractInfoService.insertByBo(bo);
    }

    @Override
    @DSTransactional
    public Boolean edit(ContractInfoBo bo) {
        ContractInfoVo contractInfoVo = contractInfoService.queryById(bo.getId());
        if (contractInfoVo == null) {
            throw new UserException("合同不存在");
        }
        if (!contractInfoVo.getOpportunityId().equals(bo.getOpportunityId())) {
            throw new UserException("合同的商机不可以修改");
        }
        if (!contractInfoVo.getCustomerId().equals(bo.getCustomerId())) {
            throw new UserException("合同的客户不可以修改");
        }
        return contractInfoService.updateByBo(bo);
    }
}
