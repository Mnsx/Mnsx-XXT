package top.mnsx.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.mnsx.enums.IsDeletedEnum;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "creator", String.class, "Mnsx_x");
        this.strictInsertFill(metaObject, "isDeleted", Integer.class, IsDeletedEnum.NO.getCode());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
