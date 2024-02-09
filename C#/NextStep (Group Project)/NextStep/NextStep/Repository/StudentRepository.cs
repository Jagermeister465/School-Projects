using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using NextStep.Data;
using NextStep.Models;
using NextStep.Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;

namespace NextStep.Repository
{
    public class StudentRepository : IStudentRepository

    {
        private readonly NextStepContext _context;

        public StudentRepository(NextStepContext context)
        {
            _context = context;
        }

        public void Dispose()
        {
            _context.Dispose();
        }

        public Student InsertStudent(StudentViewModel viewModel)
        {
            Student student = new()
            {
                FirstName = viewModel.FirstName,
                LastName = viewModel.LastName,
                ExpectedGraduation = viewModel.ExpectedGraduation,
                GPA = viewModel.GPA,
                Major = viewModel.Major
            };
            _context.Add(student);
            _context.SaveChanges();
            return student;
        }

        public Comment InsertComment(StudentViewModel studentViewModel)
        {
            Student student = _context.Student.Find(studentViewModel.Id);
            Comment comment = new()
            {
                Student = student,
                CommentDate = studentViewModel.CommentDate,
                Text = studentViewModel.Comment,
                ApplicationUser = studentViewModel.CommentedBy
            };
            _context.Comment.Add(comment);
            _context.SaveChanges();
            return comment;
        }

        public StudentViewModel UpdateStudent([Bind("Id,FirstName,LastName,ExpectedGraduation,Major,GPA")] StudentViewModel studentViewModel)
        {
            try
            {
                Student student = _context.Student.Find(studentViewModel.Id);

                student.Id = studentViewModel.Id;
                student.FirstName = studentViewModel.FirstName;
                student.LastName = studentViewModel.LastName;
                student.ExpectedGraduation = studentViewModel.ExpectedGraduation;
                student.Major = studentViewModel.Major;
                student.GPA = studentViewModel.GPA;

                _context.Update(student);
                _context.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!StudentExists(studentViewModel.Id))
                {
                    return studentViewModel;
                }
            }
            return studentViewModel;
        }

        public void DeleteStudent(int id)
        {
            var student = _context.Student.Include(c => c.Comments).FirstOrDefault(i => i.Id == id);
            _context.Comment.RemoveRange(student.Comments);
            _context.Student.Remove(student);
            _context.SaveChanges();
        }

        public void DeleteComment(int id)
        {
            var comment = _context.Comment.Find(id);
            _context.Comment.Remove(comment);
            _context.SaveChanges();
        }

        public Student GetStudentByID(int id)
        {
            return _context.Student
                .Include(c => c.Comments.OrderByDescending(d => d.CommentDate))
                .ThenInclude(a => a.ApplicationUser)
                .FirstOrDefault(i => i.Id == id);
        }

        public StudentViewModel GetStudents(StudentViewModel studentViewModel)
        {
            IQueryable<string> majorQuery = from s in _context.Student
                                            orderby s.Major
                                            select s.Major;

            var students = from s in _context.Student
                           select s;

            if (!String.IsNullOrEmpty(studentViewModel.SearchFirst))
            {
                students = students.Where(s => s.FirstName.Contains(studentViewModel.SearchFirst));
            }

            if (!string.IsNullOrEmpty(studentViewModel.StudentMajor))
            {
                students = students.Where(x => x.Major == studentViewModel.StudentMajor);
            }

            if (studentViewModel.MinimumGPA != 0.0m)
            {
                students = students.Where(x => (x.GPA.CompareTo(studentViewModel.MinimumGPA)) > 0);
            }

            return new StudentViewModel
            {
                Majors = new SelectList(majorQuery.Distinct().ToList()),
                Students = students.ToList(),
                MinimumGPA = 0.0M
            };

        }

        private bool StudentExists(int id)
        {
            return _context.Student.Any(e => e.Id == id);
        }
    }
}
