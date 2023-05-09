package top.mnsx.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum StatusEnum {
    ON(1),
    OFF(0);

    @Getter
    Integer code;

    StatusEnum(Integer code) {
        this.code = code;
    }
}
