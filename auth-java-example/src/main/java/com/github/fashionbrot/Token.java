package com.github.fashionbrot;

import lombok.Data;

@Data
public class Token extends AuthEncoder{

    private Integer userId;

    private String username;

}
