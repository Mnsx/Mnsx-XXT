package top.mnsx.enums;

import lombok.Getter;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum EnabledEnum {

    YES(1, "启用"),
    NO(0, "停用");

    @Getter
    private Integer code;

    @Getter
    private String description;

    private EnabledEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(Integer code) {
        EnabledEnum[] values = EnabledEnum.values();
        for (EnabledEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}