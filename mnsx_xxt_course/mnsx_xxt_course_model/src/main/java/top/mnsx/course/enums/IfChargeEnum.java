package top.mnsx.course.enums;

import lombok.Getter;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum IfChargeEnum {
    CHARGE(1),
    NO_CHARGE(0);

    @Getter
    private Integer code;

    IfChargeEnum(Integer code) {
        this.code = code;
    }
}
