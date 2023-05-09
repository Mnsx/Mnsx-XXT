package top.mnsx.model;

import lombok.Data;
import lombok.ToString;

/**
 * 分页参数类
 * @Author Mnsx_x xx1527030652@gmail.com
 */
@Data
@ToString
public class PageParams {
    //当前页码默认值
    public static final long DEFAULT_PAGE_CURRENT = 1L;
    //每页记录数默认值
    public static final long DEFAULT_PAGE_SIZE = 10L;

    //当前页码
    private Long pageNum = DEFAULT_PAGE_CURRENT;

    //每页记录数默认值
    private Long pageSize = DEFAULT_PAGE_SIZE;

    public PageParams(){

    }

    public PageParams(long pageNum,long pageSize){
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
