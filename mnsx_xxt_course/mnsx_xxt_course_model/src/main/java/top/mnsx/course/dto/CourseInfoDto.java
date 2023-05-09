package top.mnsx.course.dto;

import lombok.Data;
import top.mnsx.model.po.CourseInfo;

/**
 * 课程基本信息dto
 */
@Data
public class CourseInfoDto extends CourseInfo {

 /**
  * 价格
  */
 private Double price;


 /**
  * 原价
  */
 private Double originalPrice;

 /**
  * 咨询qq
  */
 private String qq;

 /**
  * 微信
  */
 private String wechat;

 /**
  * 电话
  */
 private String phone;

 /**
  * 大分类名称
  */
 private String firstCategory;

 /**
  * 小分类名称
  */
 private String secondCategory;

}
