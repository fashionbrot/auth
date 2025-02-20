package com.github.fashionbrot.algorithms;




import com.github.fashionbrot.AuthEncoder;

public abstract class Algorithm {


    public abstract  <T extends AuthEncoder> T decrypt(Class<T> clazz,String token);

    public abstract String encrypt(AuthEncoder authEncoder);


}
