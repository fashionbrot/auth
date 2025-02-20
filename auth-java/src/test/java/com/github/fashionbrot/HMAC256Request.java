package com.github.fashionbrot;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class HMAC256Request extends AuthEncoder{

    private Long userId;

    private String mobile;

}
