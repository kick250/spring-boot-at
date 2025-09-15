CREATE TABLE enrollments (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     student_id BIGINT NOT NULL,
     course_id BIGINT NOT NULL,
     grade DECIMAL(3,1) CHECK (grade >= 0 AND grade <= 10),
     CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id),
     CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses(id),
     CONSTRAINT uc_enrollment UNIQUE (student_id, course_id)
);