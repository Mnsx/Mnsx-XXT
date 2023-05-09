package top.mnsx.auth.dto;

import lombok.Data;
import top.mnsx.model.po.User;

@Data
public class LoginDto extends User {
    private Integer loginType;
    private String code;
}
