package com.github.fashionbrot;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TokenModel {
    private Integer userId;

    private Date date;

    private String str;

    private String[] array;

    private List<Integer> list;

    private Map<String,Object> map;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


    @Override
    public String toString() {
        return "TokenModel{" +
                "userId=" + userId +
                ", date=" + date +
                ", str='" + str + '\'' +
                ", array=" + Arrays.toString(array) +
                ", list=" + list +
                ", map=" + map +
                '}';
    }
}
