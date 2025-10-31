package com.nvtdevmaster.lession03.service;
import com.nvtdevmaster.lession03.entity.Student;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Service class: StudentService
 * <p>Lớp dịch vụ thực hiện các chức năng thao tác với List
 Object Student</p>
 *
 * @author Admin
 * @version 1.0
 */
@Service
public class ServiceStudent {
    List<Student> students
    = new ArrayList<Student>();
    public ServiceStudent() {
        students.addAll(Arrays.asList(
                new Student(1L,"Devmaster 1",20,"Non","Số 25VNP","0978611889","chungtrinhj@gmail.com"),
                new Student(2L,"Devmaster 2",25,"Non","Số 25VNP","0978611889","contact@devmaster.edu.vn"),
                new Student(3L,"Devmaster 3",22,"Non","Số 25VNP","0978611889","chungtrinhj@gmail.com")
));
    }
    // Lấy toàn bộ danh sách sinh viên
    public List<Student> getStudents() {
        return students;
    }
    // Lấy sinh viên theo id
    public Student getStudent(Long id) {
        return students.stream()
                .filter(student -> student
                        .getId().equals(id))
                .findFirst().orElse(null);
    }
    // Thêm mới một sinh viên
    public Student addStudent(Student student) {
        students.add(student);
        return student;
    }
    // Cập nhật thông tin sinh viên
    public Student updateStudent(Long id, Student student)
    {
        Student check = getStudent(id);
        if (check == null) {
            return null;
        }
        students.forEach(item -> {if (item.getId()==id) {
                item.setName(student.getName());
                item.setAddress(student.getAddress());
                item.setEmail(student.getEmail());
                item.setPhone(student.getPhone());
                item.setAge(student.getAge());
                item.setGender(student.getGender());
            }
        });
        return student;
    }
    // Xóa thông tin sinh viên
    public boolean deleteStudent(Long id){
        Student check = getStudent(id);
        return students.remove(check);
    }
}
