package com.pzhu.iacaa2_0.entityVo;

import com.pzhu.iacaa2_0.entity.CheckLink;
import com.pzhu.iacaa2_0.entity.Course;
import com.pzhu.iacaa2_0.entity.CourseTask;
import lombok.Data;
import java.util.List;

/**
 * @author MrZhao
 */
@Data
public class CourseVo extends Course {
    private String word;
    private List<CourseTask> courseTasks;
    private List<CheckLink> checkLinks;
}
