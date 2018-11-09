/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.callback;

import cn.sipin.cloud.merchandise.client.service.MaterialService;
import cn.sipin.cloud.merchandise.request.MaterialRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

import org.springframework.stereotype.Component;

@Component
public class MaterialServiceFallBack implements MaterialService {

  @Override public ResponseData index(MaterialRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData indexWithoutStock(MaterialRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getSkusBySkuNos(String skuNos) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getSkusBySkuSns(String skuSns) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }

  @Override public ResponseData getUnableSkus() {
    return ResponseData.build(
            ResponseBackCode.ERROR_DOWNGRADE.getValue(),
            ResponseBackCode.ERROR_DOWNGRADE.getMessage());
  }
}
