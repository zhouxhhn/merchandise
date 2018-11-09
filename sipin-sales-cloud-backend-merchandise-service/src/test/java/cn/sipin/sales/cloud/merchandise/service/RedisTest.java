/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service;

import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import cn.sipin.cloud.merchandise.pojo.Material;
import cn.sipin.cloud.merchandise.response.MaterialResponse;
import cn.sipin.sales.cloud.merchandise.service.request.MaterialListRequest;
import cn.sipin.sales.cloud.merchandise.service.service.RedisClusterServiceContract;
import cn.sipin.sales.cloud.merchandise.service.service.MaterialServiceContract;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class RedisTest {

  @Autowired
  private RedisClusterServiceContract lettuceClusterService;

  @Autowired
  private MaterialServiceContract materialService;

  @Test
  public void testRedisTest() {

    lettuceClusterService.set("jinsheng", "hello world this is test 8888886666");

    System.out.println("缓存开始：" + lettuceClusterService.get("jinsheng"));
  }

  @Test
  public void testMaterialService() {

    MaterialListRequest request = new MaterialListRequest();
    for (int i = 1; i < 10; i++) {
      Page<MaterialResponse> materialPage = new Page<MaterialResponse>(i, 15);
      materialPage.setAsc(false);

      Page<MaterialResponse> materialPage1 = materialService.selectMaterialPage(materialPage, request);
    }

    Material material = new Material();
    material.setMaterialId(7327373773L);
    material.setNumber("8883838238001");
    material.setName("何鲁丽");
    material.setSku("8738738");
    material.setSpu("838383");
    material.setTexture("规格为1，颜色为大哭大哭大哭");
    material.setAmount(new BigDecimal("233.00"));
    material.setColor("红色");
    material.setSpecification("二人位+中间位+贵妃位（左）");
    material.setStatus(0);
    material.setForbidStatus(1);
    material.setImgPath("https://o9zv5sm0o.qnssl.com/2017/12/09/5a2b50e411d882929883288.jpg");

    materialService.insertIgnore(material);
  }

  @Test
  public void testCode() {

    String rawString = "abcdOPQRSTmnop01234qrstuMNUVWvwxyzABGHIefghi567jklJKCDEFLXYZ89";

    DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Integer dayOfYear = LocalDate.now().getDayOfYear();
    //得到当前日期是在这年的第几天
    System.out.println(dayOfYear);

    Integer position = dayOfYear % 62;

    char prefix = rawString.charAt(position);
    Integer number = 6;
    //生成指定长度的字母和数字的随机组合字符串
    String postfixCode = RandomStringUtils.randomAlphanumeric(number - 1);
    System.out.println(postfixCode);

    String code = new StringBuilder().append(prefix).append(postfixCode).toString();

    System.out.println("code等于" + code);
  }

  @Test
  public void selectSkuOnlySnAndNoList() {
    materialService.selectBySns(null);
  }
}
