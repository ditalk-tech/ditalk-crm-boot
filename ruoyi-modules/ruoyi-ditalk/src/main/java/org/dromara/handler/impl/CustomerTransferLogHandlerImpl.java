package org.dromara.handler.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.constant.CommonConstants;
import org.dromara.common.mybatis.helper.DataPermissionHelper;
import org.dromara.handler.ICustomerInfoCommonHandler;
import org.dromara.handler.ICustomerTransferLogHandler;
import org.dromara.module.customer.domain.bo.CustomerTransferLogBo;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerTransferLogService;
import org.springframework.stereotype.Service;

/**
 * 系统用户应用接口
 *
 * @author weidixian
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerTransferLogHandlerImpl implements ICustomerTransferLogHandler {

    private final ICustomerTransferLogService customerTransferLogService;
    private final ICustomerInfoCommonHandler customerInfoCommonHandler;

    @Override
    @DSTransactional
    public void add(Long customerId, Long newUserId, Long newDeptId) {
        CustomerInfoVo infoVo = DataPermissionHelper.ignore(() -> customerInfoCommonHandler.queryAllByIdNoCache(customerId));
        CustomerTransferLogBo logBo = new CustomerTransferLogBo();
        logBo.setCustomerId(customerId);
        logBo.setOldUserId(infoVo.getAssignedTo());
        logBo.setOldDeptId(infoVo.getAssignedDept());
        logBo.setNewUserId(newUserId);
        logBo.setNewDeptId(newDeptId);
        logBo.setState(CommonConstants.AVAILABLE);
        Boolean flag = customerTransferLogService.insertByBo(logBo);
        if (!flag) throw new RuntimeException("记录客户转移日志失败");
    }
}
