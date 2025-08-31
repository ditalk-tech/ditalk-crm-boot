package org.dromara.handler.impl;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.lock.annotation.Lock4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.user.UserException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.enums.ContractStateEnum;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.utils.NumberConverterUtil;
import org.dromara.handler.IContractInfoHandler;
import org.dromara.module.contract.domain.bo.ContractInfoBo;
import org.dromara.module.contract.domain.vo.ContractInfoVo;
import org.dromara.module.contract.service.IContractInfoService;
import org.dromara.module.customer.domain.vo.CustomerInfoVo;
import org.dromara.module.customer.service.ICustomerInfoService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public String generateContractCode(String prefix, String datePattern, String separator, String serial) {
        StringBuilder codeBuilder = new StringBuilder()
            .append(prefix);
        // 加入日期
        codeBuilder.append(separator).append(DateUtil.format(new Date(), datePattern));
        // 加入自定义的流水号
        if (StringUtils.isNotBlank(serial)) {
            codeBuilder.append(separator).append(serial);
        }
        // 加入雪花ID防重复
        Long id = IdUtil.getSnowflakeNextId();
        String suffix = NumberConverterUtil.numToRadix(id.toString(), 36);
        codeBuilder.append(separator).append(suffix);
        return codeBuilder.toString();
    }

    /**
     * 新增合同信息，当前用户和部门自动赋值，状态默认为草稿。
     *
     * @param bo 合同信息业务对象
     * @return 是否新增成功
     */
    @Override
    @DSTransactional
    @Lock4j(name="ContractInfoAdd", keys = {"#bo.code"}, expire = 5000L)
    public Boolean add(ContractInfoBo bo) {
        CustomerInfoVo customerInfoVo = customerInfoService.queryById(bo.getCustomerId()); // 线索不可以创建合同
        if (customerInfoVo == null) {
            throw new UserException("客户不存在，无法创建合同");
        }
        ContractInfoBo codeQuery = new ContractInfoBo();
        codeQuery.setCode(bo.getCode());
        List<ContractInfoVo> infoVos = contractInfoService.queryList(codeQuery);
        if (IterUtil.isNotEmpty(infoVos)) {
            throw new UserException("合同编号生成失败，请重试");
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
        ContractInfoVo infoVo = contractInfoService.queryById(bo.getId());
        if (!infoVo.getCode().equals(bo.getCode())) {
            throw new UserException("合同编号不可以修改");
        }
        return contractInfoService.updateByBo(bo);
    }
}
