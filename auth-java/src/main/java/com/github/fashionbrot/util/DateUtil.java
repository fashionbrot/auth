package com.github.fashionbrot.util;

import java.util.Date;

public class DateUtil {

    public static boolean isDateBetweenInclusive(Date dateToCheck, Date startDate, Date endDate) {
        return !(dateToCheck.before(startDate) || dateToCheck.after(endDate));
    }


}
