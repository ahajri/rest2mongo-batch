import * as React from "react";
import { RouteComponentProps } from "react-router";
import * as moment from "moment";
import { CalendarEvent } from "../calendar/calendar-models"
import { EventsList } from "./EventsList";
import { Calendar, DisplayMode } from "../calendar/Calendar";

export interface IMainComponentProps
{
	allEvents: CalendarEvent[] | null;
	displayMode: DisplayMode;
	onChange?: (events: CalendarEvent[]) => void;
	onDisplayModeChange?: (mode: DisplayMode) => void;
}

interface IMainComponentState
{
	filteredEvents: CalendarEvent[] | null;
	searchText: string;
	currentDate: moment.Moment;
}

export class MainComponent extends React.Component<IMainComponentProps & RouteComponentProps<{}>, IMainComponentState> {

	private searchInput: HTMLInputElement | null;

	constructor(props: IMainComponentProps & RouteComponentProps<{}>)
	{
		super(props);

		this.state = {
			searchText: "",
			filteredEvents: null,
			currentDate: moment(new Date()).startOf("day")
		};
	}

	componentDidMount()
	{
		if (!!this.searchInput)
		{
			this.searchInput.select();
			this.searchInput.focus();
		}
	}

	private onSearchTextChanged(e: React.ChangeEvent<HTMLInputElement>)
	{
		this.setState({ ...this.state, searchText: e.target.value });
	}

	private onSearchClicked()
	{
		if (!!this.searchInput)
			this.searchInput.selectionStart = this.searchInput.value.length;

		if (!!this.state.searchText && !!this.props.allEvents)
		{
			let searchText = this.state.searchText.toLowerCase();
			let filteredEvents = this.props.allEvents.filter(x => !!x.title && x.title.toLowerCase().indexOf(searchText) !== -1);
			filteredEvents.forEach(x => x.isSelected = false);

			this.setState({ ...this.state, filteredEvents: filteredEvents });
		}
	}

	private onCalendarEventClicked(event: CalendarEvent)
	{
		//alert("Clicked " + event.title);
	}

	private onListEventClicked(event: CalendarEvent)
	{
		if (!!this.state.filteredEvents)
		{
			let newFilteredEvents = this.toggleEventSelection(this.state.filteredEvents as Array<CalendarEvent>, event.id);

			this.setState({ ...this.state, filteredEvents: newFilteredEvents });
		}

		if (!!this.props.allEvents && !!this.props.onChange)
		{
			let newAllEvents = this.toggleEventSelection(this.props.allEvents as Array<CalendarEvent>, event.id);

			this.props.onChange(newAllEvents);
		}
	}

	private toggleEventSelection(events: Array<CalendarEvent>, id: number): Array<CalendarEvent>
	{
		return events.map((event) =>
		{
			let newEvent = event.clone();
			if (event.id === id)
				newEvent.isSelected = !event.isSelected;
			else
				newEvent.isSelected = false;

			return newEvent;
		});
	}

	private onSearchKeyDown(e: React.KeyboardEvent<HTMLElement>)
	{
		if (e.keyCode === 13)
			this.onSearchClicked();
	}

	private setDisplayMode(mode: DisplayMode)
	{
		if (!!this.props.onDisplayModeChange)
			this.props.onDisplayModeChange(mode);
	}

	private go(forward: boolean)
	{
		let value = forward ? 1 : -1;
		let unit: moment.DurationInputArg2;

		switch (this.props.displayMode)
		{
			case DisplayMode.Day:
				unit = "days";
				break;
			case DisplayMode.Week:
				unit = "weeks";
				break;
			case DisplayMode.Month:
			default:
				unit = "months";
				break;
		}

		let newDate = moment(this.state.currentDate).add(value, unit);

		this.setState({ ...this.state, currentDate: newDate });
	}

	private goBack()
	{
		this.go(false);
	}

	private goForward()
	{
		this.go(true);
	}

	private goToday()
	{
		let newDate = moment(new Date()).startOf("day");

		this.setState({ ...this.state, currentDate: newDate });
	}

	private renderCalendar(events: CalendarEvent[] | null)
	{
		let monthClasses = "btn btn-default" + (this.props.displayMode === DisplayMode.Month ? " active" : "");
		let weekClasses = "btn btn-default" + (this.props.displayMode === DisplayMode.Week ? " active" : "");
		let dayClasses = "btn btn-default" + (this.props.displayMode === DisplayMode.Day ? " active" : "");

		let title: string;

		if (this.props.displayMode === DisplayMode.Week)
		{
			let firstDay = moment(this.state.currentDate).startOf("isoWeek");
			let lastDay = moment(this.state.currentDate).endOf("isoWeek");
			title = firstDay.format("DD/MM") + " - " + lastDay.format("DD/MM");
		}
		else
			title = this.state.currentDate.format("MMMM, YYYY");

		return <div>
			       <div className="flex-row margin-bottom-lg calendar-buttons">

						<button className="btn btn-default" onClick={e => this.goBack()}>Previous</button>
				       <button className="btn btn-success" onClick={e => this.goToday()}>Today</button>
				       <button className="btn btn-default" onClick={e => this.goForward()}>Next</button>
				       <div className="flex flex-row flex-align-center flex-justify-center calendar-title">{title}</div>
				       <button className={monthClasses} onClick={e => this.setDisplayMode(DisplayMode.Month)}>Month</button>
				       <button className={weekClasses} onClick={e => this.setDisplayMode(DisplayMode.Week)}>Week</button>
				       <button className={dayClasses} onClick={e => this.setDisplayMode(DisplayMode.Day)}>Day</button>

				       <br />
				   </div>

				   <Calendar mode={this.props.displayMode} currentDate={this.state.currentDate} events={events} onEventClicked={(e) => this.onCalendarEventClicked(e)} cellHeight={21} />

		       </div>;
	}

	private renderEventsTable(events: CalendarEvent[])
	{
		return <EventsList events={this.state.filteredEvents} onEventClicked={(e) => this.onListEventClicked(e)} />;
	}

	public render()
	{
		let contents = !!this.state.filteredEvents ? this.renderEventsTable(this.state.filteredEvents as Array<CalendarEvent>) : null;
		return <div className="content">
					{this.renderCalendar(this.state.filteredEvents)}
			       <h4>Search Events</h4>
			       <div className="input-group search-input">
				       <input type="text" className="form-control" placeholder="Search for..." value={this.state.searchText} ref={el => this.searchInput = el} onChange={e => this.onSearchTextChanged(e)} onKeyDown={e => this.onSearchKeyDown(e)} />
				       <span className="input-group-btn">
					       <button className="btn btn-default" type="button" onClick={e => this.onSearchClicked()}>
						       <span className="glyphicon glyphicon-search"></span>
					       </button>
				       </span>
			       </div>
			       {contents}
		       </div>;
	}
}
