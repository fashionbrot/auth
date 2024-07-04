package com.github.fashionbrot;

import lombok.Data;

@Data
public class HMAC256Request extends AuthEncoder{

    private Long userId;

    private String mobile;

}
