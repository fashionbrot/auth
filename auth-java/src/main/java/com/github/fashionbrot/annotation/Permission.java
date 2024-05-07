package com.github.fashionbrot.annotation;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    String[] value();
}
