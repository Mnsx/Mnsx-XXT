package top.mnsx.enums;

import lombok.Getter;

public enum IfChargeEnum {
    CHARGE(1),
    NO_CHARGE(0);

    @Getter
    private Integer code;

    IfChargeEnum(Integer code) {
        this.code = code;
    }
}
