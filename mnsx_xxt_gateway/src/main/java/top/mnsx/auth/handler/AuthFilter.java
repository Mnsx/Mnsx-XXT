package top.mnsx.auth.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.mnsx.auth.po.WhiteList;
import top.mnsx.config.RedisConstant;
import top.mnsx.model.RestResponse;
import top.mnsx.utils.JwtUtil;

import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String TOKEN_KEY = "token";

    @Autowired
    private StringRedisTemplate template;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        String requestPath = exchange.getRequest().getPath().value();
        // 判断是否符合白名单
        if (validateWhiteList(requestPath)) {
            return chain.filter(exchange);
        }
        List<String> tokenList = exchange.getRequest().getHeaders().get(TOKEN_KEY);

        // 验证token合法性
        boolean ifPass = true;
        // 判断是否包含token
        if (tokenList != null && !tokenList.isEmpty() && !tokenList.get(0).trim().isEmpty()) {
            String token = tokenList.get(0).trim();
            try {
                // 解析token
                int id = Integer.parseInt(JwtUtil.parseJWT(token).getSubject());
                template.hasKey(RedisConstant.KEY_AUTH_LOGIN + id);
                return chain.filter(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // token不存在 或者 token错误
        ServerHttpResponse response = exchange.getResponse();
        byte[] data = JSON.toJSONBytes(RestResponse.validFail(null, "token为空"));
        DataBuffer wrap = response.bufferFactory().wrap(data);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 判断路径是否属于白名单
     * @param requestPath 请求路径
     * @return boolean
     */
    private boolean validateWhiteList(String requestPath) {
        List<Object> values = template.boundHashOps(RedisConstant.KEY_GATEWAY_WHITE_LIST).values();
        if (values != null) {
            for (Object value : values) {
                WhiteList whiteList = JSON.parseObject(String.valueOf(value), WhiteList.class);
                log.info("{}", whiteList);
                if (requestPath.contains(whiteList.getPath()) || requestPath.matches(whiteList.getPath())) {
                    return true;
                }
            }
        }
        return false;
    }
}
