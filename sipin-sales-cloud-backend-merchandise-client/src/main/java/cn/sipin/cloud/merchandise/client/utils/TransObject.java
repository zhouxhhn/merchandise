/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.merchandise.client.utils;

import com.google.gson.Gson;

public class TransObject<T> {

  public T convertObject(Object current,Class newObject){
    Gson gson=new Gson();
    String dataJson = gson.toJson(current);
    return (T)gson.fromJson(dataJson,newObject);
  }
}
