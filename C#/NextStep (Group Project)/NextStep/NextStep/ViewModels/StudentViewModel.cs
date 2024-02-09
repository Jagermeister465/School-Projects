using Microsoft.AspNetCore.Mvc.Rendering;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace NextStep.Models
{
    public class StudentViewModel
    {
        public StudentViewModel()
        {
            //Empty Constructor
        }

        public StudentViewModel(Student student)
        {
            if (student != null)
            {
                Id = student.Id;
                FirstName = student.FirstName;
                LastName = student.LastName;
                ExpectedGraduation = student.ExpectedGraduation;
                Major = student.Major;
                GPA = student.GPA;
                Comments = student.Comments;
            }
        }

        public int Id { get; set; }

        [Required, Display(Name = "First Name")]
        public string FirstName { get; set; }

        [Required, Display(Name = "Last Name")]
        public string LastName { get; set; }

        [DataType(DataType.Date), Display(Name = "Expected Graduation")]
        public DateTime ExpectedGraduation { get; set; }

        public string Major { get; set; }

        [Column(TypeName = "decimal(3,2)")]
        public decimal GPA { get; set; }

        [Display(Name = "Comment")]
        public string Comment { get; set; }

        [Display(Name = "User")]
        public ApplicationUser CommentedBy { get; set; }

        [Display(Name = "Date")]
        public DateTime CommentDate { get; set; }



        public List<Student> Students { get; set; }

        public IList<Comment> Comments { get; set; }

        public SelectList Majors { get; set; }

        public string StudentMajor { get; set; }

        public string SearchFirst { get; set; }

        public decimal MinimumGPA { get; set; }
    }
}
