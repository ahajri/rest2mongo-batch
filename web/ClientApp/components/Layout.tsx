import * as React from "react";
import { NavMenu } from "./NavMenu";
import { AuthComponent } from "./AuthComponent";
import { FetchService } from "../services/FetchService";
import { Route, RouteComponentProps } from "react-router-dom"
import { CalendarEvent } from "../calendar/calendar-models";
import * as moment from "moment";
import { MainComponent } from "./MainComponent";
import { AdminComponent } from "./AdminComponent";
import { DisplayMode } from "../calendar/Calendar";

export interface ILayoutProps
{
	children?: React.ReactNode;
	fetchService: FetchService;
}

interface ILayoutState
{
	isAuthenticated: boolean;
	isLoading: boolean;
	events: Array<CalendarEvent>;
	displayMode: DisplayMode;
}

export class Layout extends React.Component<ILayoutProps & RouteComponentProps<{}>, ILayoutState>
{
	constructor(props: ILayoutProps & RouteComponentProps<{}>)
	{
		super(props);

		this.state = {
			isAuthenticated: this.props.fetchService.isAuthenticated,
			isLoading: false,
			displayMode: DisplayMode.Week,
			events: [],
		};
	}

	componentDidMount()
	{
		this.fetchData(this.state.isAuthenticated);
	}

	private fetchData(isAuthenticated: boolean)
	{
		if (!isAuthenticated)
			return;

		this.setState({ ...this.state, isAuthenticated: true, isLoading: true });

		this.props.fetchService.getCalendarData()
			.then(data =>
			{
				data = this.prepareData(data);

				this.setState({ ...this.state, events: data, isLoading: false });
			});
	}

	private prepareData(data: Array<CalendarEvent>): Array<CalendarEvent>
	{
		data.forEach((x) =>
		{
			x.start = moment(x.startStr);
			x.end = moment(x.endStr);
		});

		return data.sort((x, y) =>
		{
			let dateCompare = (dt1: moment.Moment, dt2: moment.Moment): number => moment.utc(dt1).diff(moment.utc(dt2));

			if (x.allDay && y.allDay)
				return dateCompare(x.start, y.start);

			if (x.allDay)
				return -1;

			if (y.allDay)
				return 1;

			return dateCompare(x.start, y.end);
		});
	}

	private onLogin()
	{
		this.fetchData(true);
	}

	private onLogoffClick(e: React.FormEvent<HTMLElement>)
	{
		this.props.fetchService.logoff();

		this.setState({ isAuthenticated: false, events: [] });
	}

	private onChange(events: CalendarEvent[])
	{
		this.setState({ ...this.state, events: events });
	}

	private onDisplayModeChange(mode: DisplayMode)
	{
		this.setState({ ...this.state, displayMode: mode });
	}

	private renderNav()
	{
		let title = "Main";
		if (!!this.props.location && !!this.props.location.pathname)
		{
			if (this.props.location.pathname.toLowerCase() === "/admin")
				title = "Admin";
		}

		return <nav className="navbar navbar-default top-nav">
			<h3>{title}</h3>
			<div className="flex"></div>
			<button type="button" className="btn btn-default" onClick={e => this.onLogoffClick(e)}>Logoff</button>
		</nav>;
	}

	public render()
	{
		if (this.state.isLoading)
			return <div className="loading"></div>;

		if (!this.state.isAuthenticated)
			return <AuthComponent fetchService={this.props.fetchService} onLogin={() => this.onLogin()} />;

		return <div className="flex flex-row">
				<NavMenu />
				<div className="flex-column flex">
					{this.renderNav()}
					<Route exact path="/" render={ (props) => <MainComponent {...props} allEvents={this.state.events} onChange={(e) => this.onChange(e)} displayMode={this.state.displayMode} onDisplayModeChange={(mode) => this.onDisplayModeChange(mode)} /> } />
					<Route path="/admin" render={(props) => <AdminComponent {...props} allEvents={this.state.events} onChange={(e) => this.onChange(e)} />} />
				</div>
			</div>;
	}
}
