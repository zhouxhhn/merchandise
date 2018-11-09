/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.callback;


import org.springframework.stereotype.Component;

import cn.sipin.cloud.merchandise.client.service.SalesUserService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * 门店管理员熔断器
 */
@Component
public class SalesUserServiceFallBack implements SalesUserService {



  @Override public ResponseData getUserByToken(String token) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
