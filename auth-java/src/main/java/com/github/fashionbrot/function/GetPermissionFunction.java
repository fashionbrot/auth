package com.github.fashionbrot.function;

import java.util.Set;

/**
 * 获取数据库权限
 */
@FunctionalInterface
public interface GetPermissionFunction {

    Set<String> getPermission();

}
