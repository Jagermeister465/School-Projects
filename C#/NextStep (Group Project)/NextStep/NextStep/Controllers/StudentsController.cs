using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using NextStep.Models;
using NextStep.Repository.Interfaces;
using System.Threading.Tasks;

namespace NextStep.Controllers
{
    [Authorize]
    public class StudentsController : Controller
    {
        private IStudentRepository _studentRepository;
        private UserManager<ApplicationUser> _userManager;

        public StudentsController(UserManager<ApplicationUser> userManager, IStudentRepository respository)
        {
            _userManager = userManager;
            _studentRepository = respository;
        }

        // GET: CardView
        public IActionResult IndexCardView(StudentViewModel StudentViewModel)
        {
            return View(_studentRepository.GetStudents(StudentViewModel));
        }

        // GET: Students
        public IActionResult Index(StudentViewModel StudentViewModel)
        {
            return View(_studentRepository.GetStudents(StudentViewModel));
        }


        // GET: Students/Details/5
        public IActionResult Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }
            return View(new StudentViewModel(_studentRepository.GetStudentByID((int)id)));
        }

        // GET: Students/Create
        public IActionResult Create()
        {
            return View(new StudentViewModel());
        }

        // POST: Students/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Create(StudentViewModel student)
        {
            if (ModelState.IsValid)
            {
                _studentRepository.InsertStudent(student);
                return RedirectToAction(nameof(Index));
            }
            return View();
        }

        // GET: Students/Edit/5
        public IActionResult Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            StudentViewModel studentViewModel = new(_studentRepository.GetStudentByID((int)id));

            if (studentViewModel.Id == 0)
            {
                return NotFound();
            }

            return View(studentViewModel);
        }

        // POST: Students/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Edit(int id, StudentViewModel StudentViewModel)
        {
            if (id != StudentViewModel.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                StudentViewModel = _studentRepository.UpdateStudent(StudentViewModel);
                return RedirectToAction(nameof(Index));
            }
            return View(StudentViewModel);
        }

        // GET: Students/Delete/5
        public IActionResult Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }
            return View(new StudentViewModel(_studentRepository.GetStudentByID((int)id)));
        }

        // POST: Students/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public IActionResult DeleteConfirmed(int id)
        {
            _studentRepository.DeleteStudent(id);
            return RedirectToAction(nameof(Index));
        }

        public IActionResult DeleteComment(int commentId, int studentId)
        {
            _studentRepository.DeleteComment(commentId);
            return RedirectToAction(nameof(Details), new { id = studentId });
        }

        public IActionResult AddComment(int Id)
        {
            StudentViewModel studentViewModel = new StudentViewModel(_studentRepository.GetStudentByID(Id));

            return View(studentViewModel);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> AddComment(StudentViewModel studentViewModel)
        {
            if (ModelState.IsValid)
            {
                studentViewModel.CommentDate = System.DateTime.Now;
                studentViewModel.CommentedBy = await _userManager.GetUserAsync(User);
                _studentRepository.InsertComment(studentViewModel);
                return RedirectToAction(nameof(Details), new { studentViewModel.Id });
            }
            return RedirectToAction(nameof(Details), new { studentViewModel.Id });
        }
    }
}
