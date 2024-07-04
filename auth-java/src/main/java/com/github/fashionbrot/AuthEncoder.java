package com.github.fashionbrot;

import lombok.Data;

import java.util.Date;

@Data
public class AuthEncoder {

    /**
     * Get the value of the "iat" claim, or null if it's not available.
     *
     * @return the Issued At value or null.
     */
    private Date issuedAt;

    /**
     * Get the value of the "exp" claim, or null if it's not available.
     *
     * @return the Expiration Time value or null.
     */
    private Date expiresAt;

}
