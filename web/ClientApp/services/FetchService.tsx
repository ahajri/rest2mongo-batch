import "isomorphic-fetch";
import { LoginModel, RegisterModel } from "../models/models";
import { Promise } from "es6-promise";
import { CalendarEvent, RecurringPeriod } from "../calendar/calendar-models";
import * as moment from "moment";

export class FetchService
{
	private token: string;

	constructor()
	{
		this.loadToken();
	}

	private loadToken()
	{
		let token = localStorage.getItem("token");
		this.token = !!token ? token : "";
	}

	private saveToken(token: string)
	{
		this.token = token;
		localStorage.setItem("token", this.token);
	}

	public get isAuthenticated(): boolean
	{
		return !!this.token;
	}

	// Replace with JAVA REST
	public login(request: LoginModel) : Promise<boolean>
	{
		return new Promise<boolean>((resolve, reject) =>
		{
			if (request.remember)
				this.saveToken("some_token");
			else
				this.saveToken("");

			resolve(true);
		});

		/*return fetch("api/SampleData/Register",
			{
				method: "POST",
				headers: {
					"Accept": "application/json",
					"Content-Type": "application/json"
				},
				body: JSON.stringify(request)
			})
			.then(response => response.json() as Promise<string>)
			.then(token =>
			{
				if (request.remember)
					this.saveToken(token);
				else
				{
					this.saveToken("");
					this.token = token;
				}

				return this.isAuthenticated;
			});*/
	}

	// Replace with JAVA REST
	public register(request: RegisterModel): Promise<boolean>
	{
		return new Promise<boolean>((resolve, reject) =>
		{
			this.saveToken("some_token");

			resolve(true);
		});

		/*return fetch("api/SampleData/Login",
			{
				method: "POST",
				headers: {
					"Accept": "application/json",
					"Content-Type": "application/json"
				},
				body: JSON.stringify(request)
			})
			.then(response => response.json() as Promise<string>)
			.then(token =>
			{
				this.saveToken(token);
				return this.isAuthenticated;
			});*/
	}

	public logoff()
	{
		this.saveToken("");
	}

	// Replace with JAVA REST
	public getCalendarData(): Promise<Array<CalendarEvent>>
	{
		return new Promise<Array<CalendarEvent>>((resolve, reject) =>
		{
			let events: Array<CalendarEvent> = [];

			let monday = moment().startOf("isoWeek");
			let wednesday = moment().startOf("isoWeek").add(2, "days");
			let saturday = moment().endOf("isoWeek").add(-1, "days");

			let format = "DD.MM.YYYY HH:mm:ss";

			let event1 = new CalendarEvent();
			event1.id = 1;
			event1.title = "Event 1";
			event1.startStr = moment(monday.format("DD.MM") + ".2018 08:00:00", format).format();
			event1.endStr = moment(event1.startStr).add(2, "hours").add(-1, "minute").format();
			event1.isSelected = false;
			event1.allDay = false;
			event1.recurring = RecurringPeriod.Day;
			events.push(event1);

			let event2 = new CalendarEvent();
			event2.id = 2;
			event2.title = "Event 2";
			event2.startStr = moment(wednesday.format("DD.MM") + ".2018 10:00:00", format).format();
			event2.endStr = moment(event2.startStr).add(1, "days").add(-1, "minute").format();
			event2.isSelected = false;
			event2.allDay = false;
			event2.recurring = RecurringPeriod.None;
			events.push(event2);

			let event3 = new CalendarEvent();
			event3.id = 3;
			event3.title = "Event 3";
			event3.startStr = moment(saturday.format("DD.MM") + ".2018 10:00:00", format).format();
			event3.endStr = moment(event3.startStr).add(2, "days").add(-1, "minute").format();
			event3.isSelected = false;
			event3.allDay = false;
			event3.recurring = RecurringPeriod.None;
			events.push(event3);

			resolve(events);
		});

		/*return fetch("api/SampleData/GetSampleData",
			{
				method: "GET",
				headers: {
					"Token": this.token
				}
			});*/
	}
}