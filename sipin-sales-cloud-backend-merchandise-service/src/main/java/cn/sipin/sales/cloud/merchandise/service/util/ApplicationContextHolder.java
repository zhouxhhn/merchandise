/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.merchandise.service.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
  private static ApplicationContext applicationContext = null;

  @Override
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    // 静态变量的操作最好在静态方法里面实现
    setApplicationContextValue(ctx);
  }


  private static void setApplicationContextValue(ApplicationContext applicationContext) {
    // 避免不同的请求获取到相同ID的上下文
    if (ApplicationContextHolder.applicationContext == null ||
        !Objects.equals(ApplicationContextHolder.applicationContext, applicationContext)) {
      ApplicationContextHolder.applicationContext = applicationContext;
    }
  }



  /**
   * Get application context from everywhere
   *
   * @return
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * Get bean by class
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

  /**
   * Get bean by class name
   *
   * @param name
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) {
    return (T) applicationContext.getBean(name);
  }
}
