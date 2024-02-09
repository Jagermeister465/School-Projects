using NextStep.Models;
using System;

namespace NextStep.Repository.Interfaces
{
    public interface IStudentRepository : IDisposable
    {
        StudentViewModel GetStudents(StudentViewModel studentViewModel);
        Student GetStudentByID(int id);
        Student InsertStudent(StudentViewModel student);
        void DeleteStudent(int id);
        StudentViewModel UpdateStudent(StudentViewModel studentViewModel);
        Comment InsertComment(StudentViewModel studentViewModel);
        void DeleteComment(int id);
    }
}
