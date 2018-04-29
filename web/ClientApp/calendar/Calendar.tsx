import * as React from "react";
import * as moment from "moment";
import { CalendarEvent } from "./calendar-models";
import { CalendarService, Rect } from "./CalendarService";

export enum DisplayMode
{
	Month,
	Week,
	Day
}

export interface ICalendarProps
{
	events: Array<CalendarEvent> | null;
	currentDate: moment.Moment;
	mode: DisplayMode;
	onEventClicked?: (selectedEvent: CalendarEvent) => void;
	cellHeight: number;
}

interface ICalendarState
{
	rect: Rect | null;
}

export class Calendar extends React.Component<ICalendarProps, ICalendarState>
{
	private calendarService: CalendarService;
	private calendar: HTMLElement | null;
	private onWindowResize: any;
	private timerId: number;

	constructor(props: ICalendarProps)
	{
		super(props);

		this.state = {
			rect: null
		};

		this.calendar = null;
		this.calendarService = new CalendarService();
		this.timerId = 0;
	}

	componentDidMount()
	{
		this.updateSize();

		this.onWindowResize = this.onWindowResizeFn.bind(this);

		window.addEventListener("resize", this.onWindowResize);
	}

	componentWillUnmount()
	{
		window.removeEventListener("resize", this.onWindowResize);
	}

	componentDidUpdate()
	{
		this.updateSize();
	}

	onWindowResizeFn()
	{
		if (!!this.timerId)
			return;

		this.timerId = setTimeout(() =>
		{
			this.timerId = 0;
			this.updateSize();
		}, 30);
	}

	private updateSize()
	{
		if (!!this.calendar)
		{
			let boundingRect = this.calendar.getBoundingClientRect();

			let rect = {
				x: 0,//boundingRect.left,
				y: 0, //boundingRect.top,
				width: boundingRect.width,
				height: boundingRect.height
			};

			if (!this.state.rect || rect.x !== this.state.rect.x || rect.y !== this.state.rect.y || rect.width !== this.state.rect.width || rect.height !== this.state.rect.height)
				this.setState({ ...this.state, rect: rect });
		}
	}

	private onEventClicked(clickedEvent: CalendarEvent)
	{
		if (!!this.props.onEventClicked)
			this.props.onEventClicked(clickedEvent);
	}

	private getStartDate(firstDayOfMonth: moment.Moment)
	{
		let dayOfWeek = firstDayOfMonth.weekday();
		if (dayOfWeek === 0)
			dayOfWeek = 6;
		else
			dayOfWeek--;

		if (dayOfWeek > 0)
			return moment(firstDayOfMonth).add(-dayOfWeek, "days");
		else
			return moment(firstDayOfMonth);
	}

	private getEndDate(lastDayOfMonth: moment.Moment)
	{
		let dayOfWeek = lastDayOfMonth.weekday();
		if (dayOfWeek === 0)
			dayOfWeek = 7;

		let daysToAdd = 7 - dayOfWeek;
		if (daysToAdd > 0)
			return moment(lastDayOfMonth).add(daysToAdd, "days").endOf("day");
		else
			return moment(lastDayOfMonth).endOf("day");
	}

	private getMomentEvents(day: moment.Moment, hoursFromMidnight: number, dayEvents: Array<CalendarEvent>): Array<CalendarEvent>
	{
		let start = moment(day);
		start.set({ hour: hoursFromMidnight, minute: 0, second: 0, millisecond: 0 });

		let end = moment(start).endOf("hour");

		return dayEvents.filter(x =>
		{
			return moment(x.start).isBetween(start, end, "hours", "[]");
		});
	}

	private getDayEvents(events: Array<CalendarEvent>, day: moment.Moment, skipAllDay: boolean, skipNonAllDay: boolean): Array<CalendarEvent>
	{
		let startOfDay = moment(day).startOf("day");
		let endOfDay = moment(day).endOf("day");

		return events.filter(x =>
		{
			if (x.allDay && skipAllDay)
				return false;

			if (!x.allDay && skipNonAllDay)
				return false;

			let eventStart = moment(x.start);
			let eventEnd = moment(x.end);

			return eventStart.isBetween(startOfDay, endOfDay, "hours", "[)") || eventEnd.isBetween(startOfDay, endOfDay, "hours")
				|| (eventStart.isBefore(startOfDay) && eventEnd.isAfter(endOfDay));
		});
	}

	private isToday(day: moment.Moment): boolean
	{
		let today = moment(new Date()).startOf("day");
		return (today.diff(day, "days") === 0);
	}

	private isStartingOn(event: CalendarEvent, day: moment.Moment)
	{
		let eventStart = moment(event.start);
		if (event.allDay)
			eventStart = eventStart.startOf("day");

		let startOfDay = moment(day).startOf("day");

		let diff = moment(eventStart).diff(startOfDay, "minutes");
		if (diff >= 0 && diff < 24 * 60)
			return true;

		return false;
	}

	private isEndingOn(event: CalendarEvent, day: moment.Moment)
	{
		let eventEnd = moment(event.end);
		if (event.allDay)
			eventEnd = eventEnd.endOf("day");

		let endOfDay = moment(day).endOf("day");

		let diff = moment(endOfDay).diff(eventEnd, "minutes");
		if (diff >= 0 && diff < 24 * 60)
			return true;

		return false;
	}

	private getProjectedEvents(end: moment.Moment): Array<CalendarEvent>
	{
		if (!this.props.events)
			return [];

		return this.calendarService.getProjectedEvents(this.props.events as Array<CalendarEvent>, end);
	}

	private getEventClasses(event: CalendarEvent, day: moment.Moment)
	{
		let eventClassName = "calendar-event";
		if (event.isSelected)
			eventClassName += " selected";

		if (this.isStartingOn(event, day))
			eventClassName += " start";

		if (this.isEndingOn(event, day))
			eventClassName += " end";

		return eventClassName;
	}

	private getCalendarRect(): Rect | null
	{
		if (!this.state.rect)
			return null;

		return this.state.rect;
	}

	private renderAllDayEvents(events: Array<CalendarEvent>, day: moment.Moment): JSX.Element
	{
		let allDayEvents = this.getDayEvents(events, day, false, true).filter((x) => moment(x.start)).map((event, index) =>
		{
			let eventClassName = this.getEventClasses(event, day);

			return <div className={eventClassName} key={index} onClick={() => this.onEventClicked(event)}>{event.title}</div>;
		});

		return <div className="calendar-events">
			{allDayEvents}
		</div>;
	}

	private renderDays(week: boolean): JSX.Element
	{
		let startDate = week ? this.getStartDate(this.props.currentDate) : this.props.currentDate;
		let day = moment(startDate);

		let daysToRender = week ? 7 : 1;

		let events = this.getProjectedEvents(moment(startDate).add(daysToRender, "days"));

		/////////////////////////////////////////////////////////////////////////

		let header = [<th className="time-column"></th>];

		let cells = [<td className="time-column">All day</td>];
		for (let i = 0; i < daysToRender; i++)
		{
			let dayClass = this.isToday(day) ? "today" : "";

			header.push(<th className={dayClass}>
				{day.format("ddd DD/MM")}
			</th>);

			cells.push(<td className={dayClass}>{this.renderAllDayEvents(events, day)}</td>);
			day = moment(day).add(1, "days");
		}

		/////////////////////////////////////////////////////////////////////////

		let rows = new Array<JSX.Element>();
		rows.push(<tr>{cells}</tr>);
		cells = [];

		let start = 0;
		let end = 24;

		for (let j = start; j < end; j++)
		{
			let time = j % 2 === 0 ? j.toString() + ":00" : "";

			cells.push(<td className="time-column">{time}</td>);

			day = moment(startDate);

			for (let i = 0; i < daysToRender; i++)
			{
				let dayClass = this.isToday(day) ? "today" : "";

				cells.push(<td className={dayClass}></td>);

				day = moment(day).add(1, "days");
			}

			rows.push(<tr>{cells}</tr>);
			cells = [];
		}

		let blocks = this.calendarService.getDayViewBlocks(this.getCalendarRect(), this.props.events as Array<CalendarEvent>, startDate, daysToRender, this.props.cellHeight);

		let nodes = blocks.map((block) =>
		{
			let style = {
				left: block.rect.x + "px",
				top: block.rect.y + "px",
				width: block.rect.width + "px",
				height: block.rect.height + "px"
			};

			let classes = ["calendar-event-block"];
			if (block.event.isSelected)
				classes.push("selected");

			if (block.isStart)
				classes.push("start");

			if (block.isEnd)
				classes.push("end");

			let details = block.event.start.format('HH:mm') + ' - ' + block.event.end.format('HH:mm');

			return <div className={classes.join(' ')} style={style} onClick={() => this.onEventClicked(block.event)}>
					<span>
						{block.event.title}
					</span>
					<span>
						{details}
					</span>
				</div>;
		});

		return <div className="calendar-container">
			<table ref={(e) => { this.calendar = e }}>
				<thead>
					<tr>
						{header}
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
			{nodes}
		</div>;
	}

	private renderMonthDay(projectedEvents: Array<CalendarEvent>, day: moment.Moment): JSX.Element
	{
		let todayClassName = this.isToday(day) ? "day month-today" : "day";

		let nodes = this.getDayEvents(projectedEvents, day, false, false).map((event, index) =>
		{
			let eventClassName = this.getEventClasses(event, day);

			return <div className={eventClassName} key={index} onClick={() => this.onEventClicked(event)}>{event.title}</div>;
		});

		return <div>
			<div className={todayClassName}>{day.format("D")}</div>
			<div className="calendar-events">{nodes}</div>
		</div>;
	}

	private renderMonth(): JSX.Element
	{
		let firstDayOfMonth = moment(this.props.currentDate).startOf("month");
		let lastDayOfMonth = moment(this.props.currentDate).endOf("month");

		let startDate = this.getStartDate(firstDayOfMonth);
		let endDate = this.getEndDate(lastDayOfMonth);

		let projectedEvents = this.getProjectedEvents(endDate);

		/////////////////////////////////////////////////////////////////////////

		let rows = new Array<JSX.Element>();

		let day = moment(startDate);
		let diff = endDate.diff(day, "days");
		let days = new Array<JSX.Element>();
		let index = 0;

		while (diff >= 0)
		{
			let isGrayed = firstDayOfMonth.diff(day, "days") > 0 || lastDayOfMonth.diff(moment(day).endOf("day"), "days") < 0;

			let grayedClassName = isGrayed ? "grayed" : "";

			/////////////////////////////////////////////////////////////////////////

			days.push(<td key={index} className={grayedClassName}>{this.renderMonthDay(projectedEvents, day)}</td>);
			index++;

			day = moment(day).add(1, "days");

			if (day.weekday() === 1)
			{
				rows.push(<tr key={rows.length}>{days}</tr>);
				days = [];
			}

			if (diff === 0)
				break;

			diff = endDate.diff(day, "days");
		}

		return <div>
			<table ref={(e) => this.calendar = e}>
				<thead>
					<tr>
						<th>Mon</th>
						<th>Tue</th>
						<th>Wed</th>
						<th>Thu</th>
						<th>Fri</th>
						<th>Sat</th>
						<th>Sun</th>
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		</div>;
	}

	public render()
	{
		let className = "calendar";

		if (!this.props.events)
			return <div className={className}></div>;

		let content: JSX.Element;
		switch (this.props.mode)
		{
			case DisplayMode.Day:
				content = this.renderDays(false);
				className += " daily";
				break;

			case DisplayMode.Week:
				content = this.renderDays(true);
				className += " daily";
				break;

			case DisplayMode.Month:
			default:
				content = this.renderMonth();
				className += " monthly";
				break;
		}

		return <div className={className}>
			{content}
		</div>;
	}
}