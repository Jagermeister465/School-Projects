using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using NextStep.Data;
using System;
using System.IO;
using System.Linq;
using System.Reflection;

namespace NextStep.Models
{
    public class SeedData
    {
        public static void Initialize(IServiceProvider serviceProvider)
        {

            using (var context = new NextStepContext(
                serviceProvider.GetRequiredService<
                    DbContextOptions<NextStepContext>>()))
            {
                if (!context.Users.Any())
                {
                    SeedUsers(context);
                    context.SaveChanges();
                }


                // If no students, then seed DB
                if (!context.Student.Any())
                {
                    seedDataFile(context);
                    // DB has been seeded
                }

                context.SaveChanges();
            }
        }

        private static void SeedUsers(NextStepContext context)
        {
            string recruiter1 = "TannerH@test.com";

            var user1 = new ApplicationUser
            {
                UserName = recruiter1,
                NormalizedUserName = recruiter1.ToUpper(),
                Email = recruiter1,
                NormalizedEmail = recruiter1.ToUpper(),
                EmailConfirmed = true,
                FirstName = "Tanner",
                LastName = "Hohn"
            };


            PasswordHasher<ApplicationUser> passwordHasher = new PasswordHasher<ApplicationUser>();
            string passwordHash = passwordHasher.HashPassword(user1, "Test!1234");
            user1.PasswordHash = passwordHash;
            context.Users.Add(user1);

            string recruiter2 = "shmarf3@gmail.com";

            var user2 = new ApplicationUser
            {
                UserName = recruiter2,
                NormalizedUserName = recruiter2.ToUpper(),
                Email = recruiter2,
                NormalizedEmail = recruiter2.ToUpper(),
                EmailConfirmed = true,
                FirstName = "Whyami",
                LastName = "Doingthis"
            };


            PasswordHasher<ApplicationUser> passwordHasher2 = new PasswordHasher<ApplicationUser>();
            string passwordHash2 = passwordHasher2.HashPassword(user2, "nbfty*&^%$FG&");
            user2.PasswordHash = passwordHash2;
            context.Users.Add(user2);
        }


        private static void seedDataFile(NextStepContext context)
        {
            var assembly = Assembly.GetExecutingAssembly();

            string resourceName = "NextStep.Data.Students.csv";
            string line;

            using (Stream stream = assembly.GetManifestResourceStream(resourceName))
            using (StreamReader reader = new StreamReader(stream))
            {
                reader.ReadLine();
                while ((line = reader.ReadLine()) != null)
                {

                    string[] values = line.Split(",");

                    context.Student.Add(
                        new Student
                        {
                            FirstName = values[1],
                            LastName = values[2],
                            ExpectedGraduation = DateTime.Parse(values[3]),
                            Major = values[4],
                            GPA = decimal.Parse(values[5])
                        }
                    );
                }
            }
        }
    }
}
