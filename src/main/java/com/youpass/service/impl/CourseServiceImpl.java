package com.youpass.service.impl;

import com.youpass.dao.CourseRepository;
import com.youpass.dao.StuTakeCourseRepository;
import com.youpass.dao.StudentRepository;
import com.youpass.dao.TeacherRepository;
import com.youpass.model.CourseInfo2;
import com.youpass.model.CourseInfo3;
import com.youpass.pojo.Course;
import com.youpass.pojo.pk.CourseId;
import com.youpass.dao.*;
import com.youpass.model.CourseInfo;
import com.youpass.pojo.*;
import com.youpass.pojo.pk.CourseId;
import com.youpass.pojo.pk.StuTakeCourseId;
import com.youpass.pojo.pk.StudentId;
import com.youpass.pojo.pk.TeacherId;
import com.youpass.service.CourseService;
import com.youpass.util.ReturnType.Result.Result;
import com.youpass.util.ReturnType.Result.ResultEnum;
import com.youpass.util.ReturnType.Result.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final TeacherRepository teacherRepository;

    private final NoticeRepository noticeRepository;

    private final StudentRepository studentRepository;

    private final StuTakeCourseRepository stuTakeCourseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             TeacherRepository teacherRepository,
                             NoticeRepository noticeRepository, StudentRepository studentRepository, StuTakeCourseRepository stuTakeCourseRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.noticeRepository = noticeRepository;
        this.studentRepository = studentRepository;
        this.stuTakeCourseRepository = stuTakeCourseRepository;
    }

    @Override
    @Transactional
    public Result<Object> deleteCourse(Long teacherId, Long courseId) {
        if (courseRepository.existsById(new CourseId(courseId))) {
            var course = courseRepository.findById(new CourseId(courseId)).get();
            if (course.getTeacher().getId().getTeacherId().equals(teacherId)) {
                var teacher = teacherRepository.findById(new TeacherId(teacherId)).get();
                teacher.getCourseSet().remove(course);
                courseRepository.delete(course);
                teacherRepository.save(teacher);
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
        } else {
            return ResultUtil.error(ResultEnum.COURSE_MISS);
        }
    }

    @Override
    @Transactional
    public Result<Object> getCourseById(Long courseId) {
        if (courseRepository.existsById(new CourseId(courseId))) {
            var course = courseRepository.findById(new CourseId(courseId)).get();
            Set<Course> courses = new HashSet<>();
            courses.add(course);
            return ResultUtil.success(courses);
        } else {
            return ResultUtil.error(ResultEnum.COURSE_MISS);
        }
    }

    @Override
    @Transactional
    public Result<Object> getCourseByTitle(String title) {
        return ResultUtil.success(courseRepository.findCourseByTitle(title));
    }

    @Override
    @Transactional
    public Result<Object> getCourseByTName(String teacherName) {
        var teacher = teacherRepository.findTeacherByName(teacherName);
        Set<Course> courses = new HashSet<>();
        for(Teacher t : teacher){
            courses.addAll(t.getCourseSet());
        }
        return ResultUtil.success(courses);
    }

    @Override
    @Transactional
    public Result<Object> getCourseOfUser(Long id) {
        if (teacherRepository.existsById(new TeacherId(id))) {
            System.out.println("??????");
            var teacher = teacherRepository.findById(new TeacherId(id)).get();
            return ResultUtil.success(teacher.getCourseSet());
        } else if (studentRepository.existsById(new StudentId(id))) {
            System.out.println("??????");
            var student = studentRepository.findById(new StudentId(id)).get();
            List<Course> courseList = new ArrayList<>();
            for (var stuCourse : student.getStuTakeCourses()) {
                courseList.add(stuCourse.getCourse());
            }
            return ResultUtil.success(courseList);
        } else {
            return ResultUtil.error(ResultEnum.USER_MISS);
        }
    }

    @Override
    @Transactional
    public Result<Object> createCourse(Long teacherId, CourseInfo2 courseInfo) {
        if (teacherId == null ||
                courseInfo.getTitle() == null ||
                courseInfo.getPassword() == null) {
            return ResultUtil.error(ResultEnum.INFO_DEFICIENCY);
        }
        Long courseId = courseRepository.getNextId().orElse(courseRepository.minId);
        Optional<Teacher> teacherOptional = teacherRepository.findById(new TeacherId(teacherId));
        if (teacherOptional.isEmpty()) {
            return ResultUtil.error(ResultEnum.INFO_DEFICIENCY);
        }
        Teacher teacher = teacherOptional.get();
        Course course = Course.Builder()
                .setId(new CourseId(courseId))
                .setPassword(courseInfo.getPassword())
                .setTitle(courseInfo.getTitle())
                .setTeacher(teacher).build();

        Long noticeId = noticeRepository.getNextId(new CourseId(courseId)).orElse(noticeRepository.minId);

//        SimpleDateFormat dateFormat =new SimpleDateFormat("YYYY-MM-dd");
//        String date = dateFormat.format(new Date());
        Notice notice = Notice.Builder()
                .setId(noticeId)
                .setCourse(course)
                .setContent(courseInfo.getTitle() + "????????????")
                .setTime(new Date()).build();

        course.getNoticeSet().add(notice);
        teacher.getCourseSet().add(course);
        teacherRepository.save(teacher);
        return ResultUtil.success("????????????????????????????????????");

    }

    @Override
    @Transactional
    public Result<Object> joinCourse(Long studentId, CourseInfo3 courseInfo) {
        if (studentId == null ||
                courseInfo.getCourseId() == null ||
                courseInfo.getPassword() == null) {
            return ResultUtil.error(ResultEnum.INFO_DEFICIENCY);
        }
        //?????????studentId??????????????????????????????
        Optional<Student> studentOptional = studentRepository.findById(new StudentId(studentId));
        if (studentOptional.isEmpty()) {
            return ResultUtil.error(ResultEnum.INFO_DEFICIENCY);
        }
        Set<StuTakeCourse> stuTakeCourseSet = studentOptional.get().getStuTakeCourses();
        for (StuTakeCourse stuTakeCourse : stuTakeCourseSet) {
            if (stuTakeCourse.getCourse().getId().getCourseId().equals(courseInfo.getCourseId())) {
                return ResultUtil.error(ResultEnum.ALREADY_EXISTS);
            }
        }
        Optional<Course> courseOptional = courseRepository.findById(new CourseId(courseInfo.getCourseId()));
        if (courseOptional.isEmpty()) {
            return ResultUtil.error(ResultEnum.INFO_DEFICIENCY);
        }
        //?????????????????????
        if (!courseOptional.get().getPassword().equals(courseInfo.getPassword())) {
            return ResultUtil.error(ResultEnum.PASSWORD_ERROR);
        }
        //??????????????????StuTakeCourse??????
        StuTakeCourse stuTakeCourse = StuTakeCourse.Builder()
                .setStudent(studentOptional.get())
                .setCourse(courseOptional.get())
                .setId(new StuTakeCourseId(studentId, courseInfo.getCourseId()))
                .build();
        Student student = studentOptional.get();
        Course course = courseOptional.get();

        student.getStuTakeCourses().add(stuTakeCourse);
        course.getStuTakeCourses().add(stuTakeCourse);

        studentRepository.save(student);
        courseRepository.save(course);
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        return ResultUtil.success("??????????????????");
    }
}
