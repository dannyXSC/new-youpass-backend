package com.youpass.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.youpass.pojo.pk.StudentId;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Student implements Serializable {
    @EmbeddedId
    private StudentId id;

    @Column(length = 16, name = "Name")
    private String name;
    @JsonIgnore
    @Column(length = 32, name = "Password")
    private String password;
    @Column(length = 32, name = "Email")
    private String email;
    @Column(length = 128, name = "Location")
    private String location;
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<ExaminationPaper> examinationPaperSet = new HashSet<>();
    //cascade = CascadeType.ALL待定
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<ExamInfo> examInfos = new HashSet<>();
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<StuTakeCourse> stuTakeCourses = new HashSet<>();

    public Student() {
        id = new StudentId();
    }

    public static Student.Builder Builder() {
        return new Builder();
    }

    public static class Builder {
        private StudentId id;
        private String name;
        private String password;
        private String email;
        private String location;

        Builder(){
            id = new StudentId();
        }

        public Builder setId(StudentId id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Student build() {
            var student = new Student();
            student.id = id;
            student.name = name;
            student.password = password;
            student.email = email;
            student.location = location;
            return student;
        }
    }

    public StudentId getId() {
        return id;
    }

    public void setId(StudentId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<ExaminationPaper> getExaminationPaperSet() {
        return examinationPaperSet;
    }

    public void setExaminationPaperSet(Set<ExaminationPaper> examinationPaperSet) {
        this.examinationPaperSet = examinationPaperSet;
    }

    public Set<ExamInfo> getExamInfos() {
        return examInfos;
    }

    public void setExamInfos(Set<ExamInfo> examInfos) {
        this.examInfos = examInfos;
    }

    public Set<StuTakeCourse> getStuTakeCourses() {
        return stuTakeCourses;
    }

    public void setStuTakeCourses(Set<StuTakeCourse> stuTakeCourses) {
        this.stuTakeCourses = stuTakeCourses;
    }
}
