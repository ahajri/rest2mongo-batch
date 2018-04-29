import * as React from "react";
import { CalendarEvent, RecurringPeriod } from "../calendar/calendar-models";
import { CalendarService } from "../calendar/CalendarService";
import * as moment from "moment";
import "react-datepicker/dist/react-datepicker.css";

export interface IEventsListProps
{
	events: Array<CalendarEvent> | null;
	showRecurring?: boolean;
	months?: number;
	onEventClicked: (clickedEvent: CalendarEvent) => void;
}

export interface IEventsListState
{

}

export class EventsList extends React.Component<IEventsListProps, IEventsListState> {

	private calendarService: CalendarService;

	constructor(props: IEventsListProps)
	{
		super(props);
		
		this.state = {
		};

		this.calendarService = new CalendarService();
	}

	private onRowClicked(e: React.FormEvent<HTMLElement>, clickedEvent: CalendarEvent)
	{
		this.props.onEventClicked(clickedEvent);

	}

	private getAllEvents() : Array<CalendarEvent> | null
	{
		if (!this.props.events)
			return this.props.events;

		if (this.props.showRecurring && !!this.props.months)
			return this.calendarService.getProjectedEvents(this.props.events as Array<CalendarEvent>, moment().add(this.props.months, "months"));

		return this.props.events;
	}

	public render()
	{
		let events = this.getAllEvents();

		if (!events || events.length === 0)
			return <div className="table-container"></div>;

		let rows = events.map((event, index) =>
			<tr key={index} className={event.isSelected ? "selected" : ""} onClick={e => this.onRowClicked(e, event)}>
				<td>
					{ event.allDay ? <input type="checkbox" className="checkbox" disabled checked={true} /> : null }
				</td>
				<td>{event.id}</td>
				<td>{event.title}</td>
				<td>{event.start.toString()}</td>
				<td>{event.end.toString()}</td>
				<td>{RecurringPeriod[event.recurring]}</td>
			</tr>
		);

		return <div className="table-container">
			       <table className="table">
				       <thead>
							<tr>
								<th>All Day</th>
							   <th>ID</th>
						       <th>Title</th>
						       <th>Start</th>
						       <th>End</th>
						       <th>Recurring</th>
					       </tr>
				       </thead>
				       <tbody>
					       {rows}
				       </tbody>
			       </table>
		       </div>;
	}
}
