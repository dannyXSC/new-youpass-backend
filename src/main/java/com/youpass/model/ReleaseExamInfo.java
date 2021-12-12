package com.youpass.model;

import com.youpass.pojo.pk.CourseId;
import com.youpass.pojo.pk.ExamId;

import java.util.Date;

/**
 * @author: 叶栩冰
 * @number: 1953348
 * @indicate: JDK 11.0.12
 */
public class ReleaseExamInfo {
    private Long courseId;
    private Long examId;
    private String title;
    private Date start_time;
    private Date end_time;

    private Integer choice_num;
    private Integer multi_choice_num;
    private Integer completion_num;
    private Integer filled_num;

    public ReleaseExamInfo() {
    }

    public ReleaseExamInfo(Long courseId, Long examId, String title, Date start_time, Date end_time, Integer choice_num, Integer multi_choice_num, Integer completion_num, Integer filled_num) {
        this.courseId = courseId;
        this.examId = examId;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.choice_num = choice_num;
        this.multi_choice_num = multi_choice_num;
        this.completion_num = completion_num;
        this.filled_num = filled_num;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Integer getChoice_num() {
        return choice_num;
    }

    public void setChoice_num(Integer choice_num) {
        this.choice_num = choice_num;
    }

    public Integer getMulti_choice_num() {
        return multi_choice_num;
    }

    public void setMulti_choice_num(Integer multi_choice_num) {
        this.multi_choice_num = multi_choice_num;
    }

    public Integer getCompletion_num() {
        return completion_num;
    }

    public void setCompletion_num(Integer completion_num) {
        this.completion_num = completion_num;
    }

    public Integer getFilled_num() {
        return filled_num;
    }

    public void setFilled_num(Integer filled_num) {
        this.filled_num = filled_num;
    }
}
