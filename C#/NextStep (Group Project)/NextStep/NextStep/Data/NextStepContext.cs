using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using NextStep.Models;

namespace NextStep.Data
{
    public class NextStepContext : IdentityDbContext<ApplicationUser>
    {
        public NextStepContext(DbContextOptions<NextStepContext> options)
            : base(options)
        {
        }

        public DbSet<Student> Student { get; set; }
        public DbSet<Comment> Comment { get; set; }
    }
}
