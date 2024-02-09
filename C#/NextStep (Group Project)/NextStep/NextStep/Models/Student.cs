using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;

namespace NextStep.Models
{
    public class Student
    {
        public int Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public DateTime ExpectedGraduation { get; set; }
        public string Major { get; set; }
        [Column(TypeName = "decimal(3,2)")]
        public decimal GPA { get; set; }

        public string GraduationDateFormatted
        {
            get
            {
                return ExpectedGraduation.ToString("MMMM yyyy");
            }
        }

        public virtual IList<Comment> Comments { get; set; }
    }
}
