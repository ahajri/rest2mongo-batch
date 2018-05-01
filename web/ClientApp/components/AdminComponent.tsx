import * as React from "react";
import * as moment from "moment";
import { CalendarEvent, RecurringPeriod } from "../calendar/calendar-models";
import { EditEvent } from "./EditEvent";
import { EventsList } from "./EventsList";
import { ConfirmPopup } from "./ConfirmPopup";

export interface IAdminComponentProps
{
	allEvents: Array<CalendarEvent> | null;
	onChange: (events: CalendarEvent[]) => void;
}

export interface IAdminComponentState
{
	showRecurring: boolean;
	isDeletePopupVisible: boolean;
	selectedEventId: number;
}

export class AdminComponent extends React.Component<IAdminComponentProps, IAdminComponentState> {

	constructor(props: IAdminComponentProps)
	{
		super(props);

		this.state = {
			showRecurring: false,
			isDeletePopupVisible: false,
			selectedEventId: -1
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
		newEvent.title = "";
		newEvent.start = null;
		newEvent.end = null;
		newEvent.recurring = RecurringPeriod.None;
		newEvent.isSelected = true;

		events.push(newEvent);

		if (!!this.props.onChange)
			this.props.onChange(events);

		this.setState({ ...this.state, selectedEventId: newEvent.id });
	}

	private onDeleteClick()
	{
		if (!this.props.allEvents)
			return;

		let selectedEvents = this.props.allEvents.filter((event) => event.isSelected);
		if (selectedEvents.length === 1)
			this.setState({ ...this.state, isDeletePopupVisible: true, selectedEventId: selectedEvents[0].id });
	}

	private onConfirmDeleteClick(confirmed: boolean): void
	{
		this.setState({ ...this.state, isDeletePopupVisible: false, selectedEventId: -1 });

		if (!this.props.allEvents)
			return;

		if (confirmed)
		{
			let editedEvents = this.props.allEvents.filter((event) => event.id === this.state.selectedEventId);
			if (editedEvents.length === 1)
			{
				let newEvents = [...this.props.allEvents];
				let index = this.props.allEvents.indexOf(editedEvents[0]);
				newEvents.splice(index, 1);

				if (!!this.props.onChange)
					this.props.onChange(newEvents);
			}
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
		let deletePopup = this.state.isDeletePopupVisible ? <ConfirmPopup onClose={(confirmed) => this.onConfirmDeleteClick(confirmed)} /> : null;

		return <div className="content admin-content">
					{this.renderButtons()}
					{this.renderEventsTable()}
					{editForm}
					{deletePopup}
		       </div>;
	}
}
