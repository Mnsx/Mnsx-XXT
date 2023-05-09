package top.mnsx.course.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 添加课程Dto
 */
@Data
public class AddCourseDto {

 @NotEmpty(message = "课程名称不能为空")
 private String courseName;

 private String tags;

 @NotEmpty(message = "课程分类不能为空")
 private String firstCategory;

 @NotEmpty(message = "课程分类不能为空")
 private String secondCategory;

 private String courseDesc;

 private String picture;

 private Integer ifCharge;

 private Double price;

 private Double originalPrice;

 private String qq;

 private String wechat;

 private String phone;
}
