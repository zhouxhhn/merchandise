package cn.sipin.cloud.merchandise.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.merchandise.client.callback.MaterialServiceFallBack;
import cn.sipin.cloud.merchandise.request.MaterialRequest;
import cn.siyue.platform.base.ResponseData;

/**
 * <p>
 * 服务类
 * </p>
 */
@FeignClient(name = "merchandise-service", fallback = MaterialServiceFallBack.class)
public interface MaterialService {

  @RequestMapping(value = "/sku/with-stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData index(MaterialRequest request);

  @RequestMapping(value = "/sku/without-stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData indexWithoutStock(MaterialRequest request);

  /**
   * 不对前端展示调用，只用于服务间调用
   */
  @RequestMapping(value = "/sku/sku-list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getSkusBySkuNos(
      @RequestParam(value = "skuNos") String skuNos
  );

  /**
   * 不对前端展示调用，只用于服务间调用
   */
  @RequestMapping(value = "/sku/sku-list-by-sn", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getSkusBySkuSns(
      @RequestParam(value = "skuSns") String skuSns
  );

  /**
   * 需要排除的物料列表
   */
  @RequestMapping(value = "/sku/unablesku-list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUnableSkus();
}
