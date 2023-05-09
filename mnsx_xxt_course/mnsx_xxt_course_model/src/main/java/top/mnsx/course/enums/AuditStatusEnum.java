package top.mnsx.course.enums;

import lombok.Getter;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum AuditStatusEnum {
    UNAUDITED(0),
    AUDITED(1),
    FAIL(2);

    @Getter
    Integer code;

    AuditStatusEnum(Integer code) {
        this.code = code;
    }
}
