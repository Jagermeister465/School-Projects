using System;
using System.ComponentModel.DataAnnotations;

namespace NextStep.Models
{
    public class Comment
    {
        public int Id { get; set; }
        [Required]
        public string Text { get; set; }
        public string CommentedBy
        {
            get { return string.Concat(ApplicationUser.FirstName, " ", ApplicationUser.LastName); }
        }
        public DateTime CommentDate { get; set; }
        public virtual Student Student { get; set; }
        public virtual ApplicationUser ApplicationUser { get; set; }
    }
}
