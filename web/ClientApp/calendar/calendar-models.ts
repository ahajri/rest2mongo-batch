import * as moment from "moment/moment";

export class CalendarEvent
{
	public id: number;
	public title: string;
	public start: moment.Moment;
	public end: moment.Moment;
	public startStr: string;
	public endStr: string;
	public isSelected: boolean;
	public allDay: boolean;
	public recurring: RecurringPeriod;

	public isStartInternal: boolean;
	public isEndInternal: boolean;

	public clone(): CalendarEvent
	{
		let event = new CalendarEvent();
		event.id = this.id;
		event.title = this.title;
		event.start = moment(this.start);
		event.end = moment(this.end);
		event.startStr = this.startStr;
		event.endStr = this.endStr;
		event.isSelected = this.isSelected;
		event.allDay = this.allDay;
		event.recurring = this.recurring;

		return event;
	}
}

export enum RecurringPeriod
{
	None = 0,
	Day = 1,
	Week = 2,
	Month = 3
}