package org.example.utils;

import java.util.Calendar;
import java.util.Date;

public class AgeCalculatorUtil {

    /**
     * 根据出生日期计算年龄
     *
     * @param birthDate 出生日期
     * @return 年龄
     */
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("出生日期不能为空");
        }

        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);

        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        // 如果当前日期小于出生日期，年龄减1
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}