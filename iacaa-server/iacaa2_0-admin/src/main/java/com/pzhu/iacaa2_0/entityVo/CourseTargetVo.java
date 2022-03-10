package com.pzhu.iacaa2_0.entityVo;

import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entity.CourseTarget;
import com.pzhu.iacaa2_0.entity.Target;
import lombok.Data;

@Data
public class CourseTargetVo extends CourseTarget{
    Long id;
    Course course;
    Target target;
    Double mix;
    Integer year;
}
