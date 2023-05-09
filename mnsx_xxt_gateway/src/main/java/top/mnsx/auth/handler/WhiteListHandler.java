package top.mnsx.auth.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.mnsx.auth.mapper.WhiteListMapper;
import top.mnsx.auth.po.WhiteList;
import top.mnsx.config.RedisConstant;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class WhiteListHandler implements ApplicationRunner {
    @Resource
    private WhiteListMapper mapper;
    @Autowired
    private StringRedisTemplate template;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[mnsx-xxt] 加载路由白名单");
        List<WhiteList> whiteLists = mapper.selectList(Wrappers.emptyWrapper());
        HashMap<String, Object> whiteListMap = new HashMap<>();
        whiteLists.forEach(whiteList -> {
            whiteListMap.put(whiteList.getId().toString(), JSON.toJSONString(whiteList));
        });
        template.boundHashOps(RedisConstant.KEY_GATEWAY_WHITE_LIST).putAll(whiteListMap);
    }
}
