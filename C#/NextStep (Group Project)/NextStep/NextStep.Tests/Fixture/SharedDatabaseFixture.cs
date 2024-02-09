using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using NextStep.Data;
using NextStep.Models;
using NextStep.Tests.Helpers;
using System;
using System.Data.Common;

namespace NextStep.Tests.Fixture
{
    public class SharedDatabaseFixture : IDisposable
    {
        private static readonly object _lock = new object();
        private static bool _databaseInitialized;

        public SharedDatabaseFixture()
        {
            Connection = new SqlConnection(@"Server=(localdb)\mssqllocaldb;Database=NextStepTests;Trusted_Connection=True;MultipleActiveResultSets=true");

            seedData();

            Connection.Open();
        }

        public void Dispose() => Connection.Dispose();

        public DbConnection Connection { get; }

        public NextStepContext CreateContext(DbTransaction transaction = null)
        {
            var context = new NextStepContext(
                new DbContextOptionsBuilder<NextStepContext>().UseSqlServer(Connection).Options);

            if (transaction != null)
            {
                context.Database.UseTransaction(transaction);
            }

            return context;
        }

        private void seedData()
        {
            lock (_lock)
            {
                if (!_databaseInitialized)
                {
                    using (var context = CreateContext())
                    {
                        context.Database.EnsureDeleted();
                        context.Database.EnsureCreated();

                        addStudents(context);
                        context.SaveChanges();
                    }

                    _databaseInitialized = true;
                }
            }
        }

        private void addStudents(NextStepContext context)
        {
            context.Student
                .AddRange(
                    new Student { FirstName = Constants.FIRST_NAME, LastName = Constants.LAST_NAME_1 },
                    new Student { FirstName = Constants.FIRST_NAME, LastName = Constants.LAST_NAME_2 },
                    new Student { FirstName = Constants.FIRST_NAME, LastName = Constants.LAST_NAME_3 });
        }
    }
}

