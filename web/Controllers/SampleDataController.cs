using System;
using System.Collections.Generic;
using System.Linq;

using Demo.Models;

using Microsoft.AspNetCore.Mvc;

namespace Demo.Controllers
{
	[Route("api/[controller]")]
	public class SampleDataController : Controller
	{
		[HttpPost("[action]")]
		public string Login([FromBody] LoginModel model)
		{
			return "token";
		}

		[HttpPost("[action]")]
		public string Register([FromBody] RegisterModel model)
		{
			return "token";
		}

		[HttpGet("[action]")]
		public IEnumerable<CalendarEvent> GetSampleData([FromQuery] string searchText)
		{
			var token = this.Request.Headers["Token"];
			if (!token.Any())
				throw new UnauthorizedAccessException();

			/*var list = Enumerable.Range(1, 5)
			                     .Select(index => new CalendarEvent
			                                      {
				                                      id = index,
				                                      startStr = DateTime.Now.AddDays(index * 2),
				                                      endStr = DateTime.Now.AddDays(index * 2)
				                                                       .AddMinutes(30),
				                                      title = "Event " + index
			                                      })
			                     .ToList();

			list[4].endStr = list[4].endStr.AddDays(2);

			list.Add(new CalendarEvent
			         {
				         id = (list.Count + 1),
				         startStr = DateTime.Now.AddDays(2),
				         endStr = DateTime.Now.AddDays(2)
				                          .AddMinutes(30),
				         title = "Event " + (list.Count + 1)
			         });
			list.Add(new CalendarEvent
			         {
				         id = (list.Count + 1),
				         startStr = DateTime.Now.AddDays(2),
				         endStr = DateTime.Now.AddDays(2)
				                          .AddMinutes(60),
				         title = "Event " + (list.Count + 1)
			         });
			list.Add(new CalendarEvent
			         {
				         id = (list.Count + 1),
				         startStr = DateTime.Now.AddDays(2).AddMinutes(20),
				         endStr = DateTime.Now.AddDays(3).AddMinutes(20)
				                          .AddMinutes(90),
				         title = "Event " + (list.Count + 1)
			         });
			list.Add(new CalendarEvent
			         {
				         id = (list.Count + 1),
				         startStr = DateTime.Now.AddDays(2),
				         endStr = DateTime.Now.AddDays(2).AddMinutes(120),
						title = "Event " + (list.Count + 1)
			         });
			list.Add(new CalendarEvent
			         {
				         id = (list.Count + 1),
				         startStr = DateTime.Now.AddDays(2),
				         endStr = DateTime.Now.AddDays(2).AddMinutes(120),
				         title = "All day event",
				         allDay = true
			         });*/

			var list = Enumerable.Range(1, 1)
			                 .Select(index => new CalendarEvent
			                                  {
				                                  id = index,
				                                  startStr = DateTime.Now.AddDays(index * 2),
				                                  endStr = DateTime.Now.AddDays(index * 2)
				                                                   .AddHours(1),
				                                  title = "Event " + index,
												  recurring = RecurringPeriod.Week
			                                  })
			                 .ToList();

			return list;
		}

		public class CalendarEvent
		{
			public int id { get; set; }
			public string title { get; set; }
			public DateTime startStr { get; set; }
			public DateTime endStr { get; set; }
			public bool allDay { get; set; }
			public RecurringPeriod recurring { get; set; }
		}

		public enum RecurringPeriod
		{
			None = 0,
			Day = 1,
			Week = 2,
			Month = 3
		}
	}
}