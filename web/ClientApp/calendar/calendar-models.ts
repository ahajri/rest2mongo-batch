import * as moment from "moment/moment";

export class CalendarEvent
{
	public id: number;
	public title: string;
	public start: moment.Moment | null;
	public end: moment.Moment | null;
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
		event.start = !!this.start ? moment(this.start) : null;
		event.end = !!this.end ? moment(this.end) : null;
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