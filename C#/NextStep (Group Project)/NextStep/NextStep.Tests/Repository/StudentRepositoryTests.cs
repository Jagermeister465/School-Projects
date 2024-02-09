using NextStep.Models;
using NextStep.Repository;
using NextStep.Repository.Interfaces;
using NextStep.Tests.Fixture;
using NextStep.Tests.Helpers;
using Xunit;

namespace NextStep.Tests
{
    public class StudentRepositoryTests : IClassFixture<SharedDatabaseFixture>
    {
        private readonly SharedDatabaseFixture _fixture;
        private readonly IStudentRepository _repo;

        public StudentRepositoryTests(SharedDatabaseFixture fixture)
        {
            _fixture = fixture;
            _repo = new StudentRepository(_fixture.CreateContext());
        }

        [Fact]
        public void Get_Students()
        {
            // Arrange.
            StudentViewModel viewModel = new();
            StudentViewModel newViewModel;

            // Act.
            newViewModel = _repo.GetStudents(viewModel);

            // Assert. This is old code i was using for reference from team 0
            Assert.Equal(3, newViewModel.Students.Count);

            // The number of inspectors should match the number of Students in the list.
            Assert.Collection(newViewModel.Students,
                s => Assert.Equal(Constants.LAST_NAME_1, s.LastName),
                s => Assert.Equal(Constants.LAST_NAME_2, s.LastName),
                s => Assert.Equal(Constants.LAST_NAME_3, s.LastName));
        }

        [Fact]
        public void Get_Students_BySearch_None()
        {
            // Arrange.
            StudentViewModel viewModel = new()
            {
                SearchFirst = "qqqxxx"
            };

            // Act.

            StudentViewModel newViewModel = _repo.GetStudents(viewModel);
            // Assert.
            Assert.Empty(newViewModel.Students);
        }

        [Fact]
        public void Get_Students_BySearch_Many()
        {
            // Arrange.
            var searchString = "Test";
            StudentViewModel viewModel = new()
            {
                SearchFirst = searchString
            };

            // Act.
            StudentViewModel newViewModel = _repo.GetStudents(viewModel);

            // Assert.
            Assert.Equal(3, newViewModel.Students.Count);

            // The number of inspectors should match the number of Students in the list.
            Assert.Collection(newViewModel.Students,
                s => Assert.Equal(Constants.LAST_NAME_1, s.LastName),
                s => Assert.Equal(Constants.LAST_NAME_2, s.LastName),
                s => Assert.Equal(Constants.LAST_NAME_3, s.LastName));
        }


        [Fact]
        public void Insert_Student()
        {
            // Arrange.
            StudentViewModel testStudent = new()
            {
                FirstName = "Test",
                LastName = "Insert"
            };

            // Act.
            Student newStudent = _repo.InsertStudent(testStudent);
            Student student1 = _repo.GetStudentByID(newStudent.Id);

            // Assert.
            Assert.Same(newStudent, student1);
            Assert.Equal(student1.LastName, newStudent.LastName);

            // Cleanup.
            _repo.DeleteStudent(newStudent.Id);
        }

        [Fact]
        public void Update_Student()
        {
            // Arrange.
            string tempLastName = "Update_Update";

            StudentViewModel student = new()
            {
                FirstName = "Test",
                LastName = "Update"
            };

            // Act.
            Student newStudent = _repo.InsertStudent(student);

            student.Id = newStudent.Id;
            student.FirstName = newStudent.FirstName;
            student.LastName = tempLastName;

            StudentViewModel student1 = _repo.UpdateStudent(student);

            // Assert.
            Assert.Equal(student1.LastName, tempLastName);

            // Cleanup.
            _repo.DeleteStudent(newStudent.Id);
        }

        [Fact]
        public void Delete_Student()
        {
            // Arrange.
            StudentViewModel student = new()
            {
                FirstName = "Test",
                LastName = "Delete",
            };

            // Act.
            Student newStudent = _repo.InsertStudent(student);

            int id = newStudent.Id;
            _repo.DeleteStudent(id);

            Student student2 = _repo.GetStudentByID(id);

            // Assert.
            Assert.Null(student2);

        }

        [Fact]
        public void Get_Student_ById()
        {
            // Arrange.
            int studentId = 1;
            StudentViewModel viewModel = new()
            {
                Id = studentId
            };
            // Act.
            Student student1 = _repo.GetStudentByID(viewModel.Id);

            // Assert.
            Assert.Equal(student1.LastName, Constants.LAST_NAME_1);
        }

        [Fact]
        public void Get_Student_ById_NotFound()
        {
            // Arrange.
            int studentId = -1;
            StudentViewModel student = new()
            {
                Id = studentId
            };
            // Act.
            Student student1 = _repo.GetStudentByID(student.Id);

            // Assert.
            Assert.Null(student1);
        }

        [Fact]
        public void Get_Student_ById_After_Insert()
        {
            // Arrange.
            StudentViewModel student1 = new()
            {
                FirstName = "Test",
                LastName = "GetById"
            };

            // Act.
            Student newStudent = _repo.InsertStudent(student1);
            Student student = _repo.GetStudentByID(newStudent.Id);

            // Assert.
            Assert.Same(newStudent, student);
            Assert.Equal(newStudent.Id, student.Id);
            Assert.Equal(student.LastName, student1.LastName);

            // Cleanup.
            _repo.DeleteStudent(newStudent.Id);
        }

        [Fact]
        public void Insert_Comment()
        {
            // made a new issue to add more unit testing for comments for next iteration
            // Arrange 
            var date = System.DateTime.Now;

            StudentViewModel student1 = new()
            {
                Comment = "Test",
                CommentDate = date
            };

            // Act
            Student student = _repo.InsertStudent(student1);
            student1.Id = student.Id;
            Comment studentComment = _repo.InsertComment(student1);

            // Assert
            Assert.Same(studentComment.Text, "Test");
            Assert.Equal(studentComment.CommentDate, date);

            // Cleanup
            _repo.DeleteComment(studentComment.Id);
            _repo.DeleteStudent(student1.Id);
        }

        [Fact]
        public void Delete_Comment()
        {
            // Arrange
            StudentViewModel student1 = new()
            {
                Comment = "Test"
            };

            // Act
            Student student = _repo.InsertStudent(student1);
            Comment studentComment = _repo.InsertComment(student1);
            _repo.DeleteComment(studentComment.Id);

            // Assert
            Assert.Null(student.Comments);

            // Cleanup
            _repo.DeleteStudent(student.Id);
        }
    }
}