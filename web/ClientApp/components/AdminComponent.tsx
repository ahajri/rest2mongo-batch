import * as React from "react";
import * as moment from "moment";
import { CalendarEvent, RecurringPeriod } from "../calendar/calendar-models";
import { CalendarService } from "../calendar/CalendarService";
import { EditEvent } from "./EditEvent";
import { EventsList } from "./EventsList";

export interface IAdminComponentProps
{
	allEvents: Array<CalendarEvent> | null;
	onChange: (events: CalendarEvent[]) => void;
}

export interface IAdminComponentState
{
	showRecurring: boolean;
}

export class AdminComponent extends React.Component<IAdminComponentProps, IAdminComponentState> {

	constructor(props: IAdminComponentProps)
	{
		super(props);

		this.state = {
			showRecurring: false
		};
	}

	private onEventChanged(changedEvent: CalendarEvent)
	{
		if (!this.props.allEvents)
			return;

		let index = -1;
		for (let i = 0; i < this.props.allEvents.length; i++)
		{
			let oldEvent = this.props.allEvents[i];
			if (oldEvent.id === changedEvent.id)
			{
				index = i;
				break;
			}
		}

		if (index !== - 1)
		{
			let events = [ ...this.props.allEvents ];
			events[index] = changedEvent;

			if (!!this.props.onChange)
				this.props.onChange(events);
		}
	}

	private onRowClicked(e: React.FormEvent<HTMLElement>, clickedRowIndex: number)
	{
		if (!this.props.allEvents)
			return;

		let events = this.props.allEvents.map((event, index) =>
		{
			let isSelected = false;
			if (index === clickedRowIndex)
				isSelected = !event.isSelected;

			let newEvent = event.clone();
			newEvent.isSelected = isSelected;
			return newEvent;
		});

		if (!!this.props.onChange)
			this.props.onChange(events);
	}

	private onAddClick()
	{
		if (!this.props.allEvents)
			return;

		let maxId = 0;

		let events = this.props.allEvents.map((event) =>
		{
			maxId = Math.max(maxId, event.id);

			let newEvent = event.clone();
			newEvent.isSelected = false;
			return newEvent;
		});

		let newEvent = new CalendarEvent();
		newEvent.id = maxId + 1;
		newEvent.title = "Event " + newEvent.id;
		newEvent.start = moment();
		newEvent.end = moment().add(1, "hour");
		newEvent.recurring = RecurringPeriod.None;
		newEvent.isSelected = true;

		events.push(newEvent);

		if (!!this.props.onChange)
			this.props.onChange(events);
	}

	private onDeleteClick()
	{
		if (!this.props.allEvents)
			return;

		let selectedEvents = this.props.allEvents.filter((event) => event.isSelected);
		if (selectedEvents.length === 1)
		{
			let events = [...this.props.allEvents];
			let index = events.indexOf(selectedEvents[0]);
			events.splice(index, 1);

			if (!!this.props.onChange)
				this.props.onChange(events);
		}
	}

	private onEventClicked(e: CalendarEvent)
	{
		if (!this.props.allEvents)
			return;

		let newEvents = this.props.allEvents.map((event) =>
		{
			let newEvent = event.clone();

			if (event.id === e.id)
				newEvent.isSelected = !event.isSelected;
			else
				newEvent.isSelected = false;

			return newEvent;
		});

		this.props.onChange(newEvents);
	}

	private onCheckboxChange(e: React.ChangeEvent<HTMLInputElement>)
	{
		this.setState({ ...this.state, showRecurring: e.target.checked });
	}

	private renderButtons()
	{
		return <div className="event-list-buttons">
			<button className="btn btn-default" onClick={(e) => this.onAddClick()}>
				<span className="glyphicon glyphicon-plus"></span> New
			</button>
			<button className="btn btn-default" onClick={(e) => this.onDeleteClick()}>
				<span className="glyphicon glyphicon-remove"></span> Delete
			</button>
			<div className="flex"></div>
			<label className="control-label">
				<input type="checkbox" className="checkbox" checked={this.state.showRecurring} onChange={(e) => this.onCheckboxChange(e)} />
				Show recurring events for the next month
			</label>
		</div>;
	}

	private renderEventsTable()
	{
		return <EventsList events={this.props.allEvents} onEventClicked={(e) => this.onEventClicked(e) } showRecurring={this.state.showRecurring} months={1} />;
	}

	public render()
	{
		if (!this.props.allEvents)
			return <div></div>;

		let selectedEvents = this.props.allEvents.filter(x => x.isSelected);
		let selected = selectedEvents.length > 0 ? selectedEvents[0] : null;

		let editForm = <EditEvent event={selected} onChange={(e) => this.onEventChanged(e)} />;

		return <div className="content admin-content">
					{this.renderButtons()}
					{this.renderEventsTable()}
			       {editForm}
		       </div>;
	}
}
