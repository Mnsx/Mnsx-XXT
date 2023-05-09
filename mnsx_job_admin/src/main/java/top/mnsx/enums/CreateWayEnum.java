package top.mnsx.enums;

import lombok.Getter;

/**
 * 创建方式枚举类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public enum CreateWayEnum {

    AUTO(1, "自动"),
    MANUAL(2, "手动");

    @Getter
    private Integer code;
    @Getter
    private String description;

    private CreateWayEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(Integer code) {
        CreateWayEnum[] values = CreateWayEnum.values();
        for (CreateWayEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
