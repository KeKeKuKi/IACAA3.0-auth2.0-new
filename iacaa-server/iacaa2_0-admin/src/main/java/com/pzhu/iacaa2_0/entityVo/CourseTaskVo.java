package com.pzhu.iacaa2_0.entityVo;

import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entity.CourseTask;
import com.pzhu.iacaa2_0.entity.Target;
import lombok.Data;

import java.util.List;

@Data
public class CourseTaskVo extends CourseTask {
    Target target;
    Course course;
    String word;
}
