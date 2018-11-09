/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import cn.sipin.cloud.merchandise.client.service.SalesUserService;
import cn.sipin.cloud.merchandise.client.utils.JsonTransformer;
import cn.sipin.cloud.merchandise.response.AgencyCodeResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Service
public class SalesUserServiceImpl {

  private SalesUserService salesUserService;

  @Autowired
  public SalesUserServiceImpl(SalesUserService salesUserService) {
    this.salesUserService = salesUserService;
  }

  public AgencyCodeResponse getUserByToken() {

    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

    ResponseData agencyData = salesUserService.getUserByToken(request.getHeader("token"));
    if (!agencyData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取门店用户信息返回失败"
      );
    }

    AgencyCodeResponse agencyInfoVo = JsonTransformer
        .getObjectMapper()
        .convertValue(agencyData.getData(), new TypeReference<AgencyCodeResponse>() {});

    return agencyInfoVo;
  }
}
