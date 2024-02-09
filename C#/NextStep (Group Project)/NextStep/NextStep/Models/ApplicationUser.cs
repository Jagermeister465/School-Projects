
using Microsoft.AspNetCore.Identity;

namespace NextStep.Models
{
    public class ApplicationUser : IdentityUser
    {
        [PersonalData]
        public string FirstName { get; set; }

        [PersonalData]
        public string LastName { get; set; }

        [PersonalData]
        public string CompanyName { get; set; }

        [PersonalData]
        public string CompanyTitle { get; set; }

    }
}
