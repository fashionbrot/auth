package com.github.fashionbrot;

import com.github.fashionbrot.annotation.Permission;

public class TestClass {


    @TestPermission(value = {"test1","test2"})
    @Permission(value = {"test1","test2"})
    public void test(){

    }



}
