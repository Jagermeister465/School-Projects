using Microsoft.AspNetCore.Hosting;

[assembly: HostingStartup(typeof(NextStep.Areas.Identity.IdentityHostingStartup))]
namespace NextStep.Areas.Identity
{
    public class IdentityHostingStartup : IHostingStartup
    {
        public void Configure(IWebHostBuilder builder)
        {
            builder.ConfigureServices((context, services) =>
            {
            });
        }
    }
}