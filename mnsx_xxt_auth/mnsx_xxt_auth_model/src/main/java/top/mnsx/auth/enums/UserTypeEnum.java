package top.mnsx.auth.enums;

import lombok.Data;
import lombok.Getter;

public enum UserTypeEnum {
    USER(0),
    ADMIN(1);

    @Getter
    private Integer code;

    UserTypeEnum(Integer code) {
        this.code  = code;
    }
}
