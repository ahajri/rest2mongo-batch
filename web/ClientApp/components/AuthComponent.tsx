import * as React from "react";
import { LoginModel, RegisterModel } from "../models/models";
import { FetchService } from "../services/FetchService";

export interface ILoginProps
{
	fetchService: FetchService;
	onLogin: () => void;
}

interface ILoginState
{
	register: boolean;
	submitted: boolean;
	emailLogin: string;
	emailRegister: string;
	passwordLogin: string;
	passwordRegister: string;
	passwordConfirm: string;
	validated: boolean;
}

export class AuthComponent extends React.Component<ILoginProps, ILoginState> {

	remember: HTMLInputElement;

	constructor()
	{
		super();

		this.state = {
			register: false,
			submitted: false,
			emailLogin: "",
			emailRegister: "",
			passwordLogin: "",
			passwordRegister: "",
			passwordConfirm: "",
			validated: false
		};
	}

	private onInputChange(e: React.ChangeEvent<HTMLInputElement>)
	{
		let newState = {
			...this.state,
			[e.target.name]: e.target.value
		};

		this.setState(newState);
	}

	private onLoginClick(e: React.FormEvent<HTMLElement>)
	{
		let request = this.getLoginRequestModel();
		if (!this.hasEmptyField(request))
		{
			this.props.fetchService.login(request).then((response) => this.props.onLogin());
		}
		else
			this.setState({ ...this.state, validated: true });
	}

	private onRegisterClick(e: React.FormEvent<HTMLElement>)
	{
		let request = this.getRegisterRequestModel();
		if (!this.hasEmptyField(request) && request.password === request.confirmPassword)
		{
			this.props.fetchService.register(request).then((response) => this.props.onLogin());
		}

		this.setState({ ...this.state, validated: true });
	}

	private getLoginRequestModel(): LoginModel
	{
		let request = new LoginModel();
		request.email = this.state.emailLogin;
		request.password = this.state.passwordLogin;
		request.remember = this.remember.checked;

		return request;
	}

	private getRegisterRequestModel(): RegisterModel
	{
		let request = new RegisterModel();
		request.email = this.state.emailRegister;
		request.password = this.state.passwordRegister;
		request.confirmPassword = this.state.passwordConfirm;

		return request;
	}

	private hasEmptyField(obj: any): boolean
	{
		let keys = Object.keys(obj);
		for (let i = 0; i < keys.length; i++)
		{
			let key = keys[i];
			let value = obj[key];
			if (typeof value === "string" && !value)
				return true;
		}

		return false;
	}

	private getInputStyles(value: string, confirmPassword: boolean = false): string
	{
		let invalid = false;

		if (this.state.validated && !value)
			invalid = true;

		if (confirmPassword && this.state.passwordRegister !== value)
			invalid = true;

		let cssClass = "form-control";
		if (invalid)
			cssClass += " invalid";

		return cssClass;
	}

	private renderLogin()
	{
		return <div className="col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
			<div className="form-group">
				<input type="email" className={this.getInputStyles(this.state.emailLogin)} placeholder="Email Address" name="emailLogin" value={this.state.emailLogin} onChange={e => this.onInputChange(e)} />
			</div>
			<div className="form-group">
				<input type="password" className={this.getInputStyles(this.state.passwordLogin)} placeholder="Password" name="passwordLogin" value={this.state.passwordLogin} onChange={e => this.onInputChange(e)} />
			</div>
			<div className="form-group text-center">
				<label><input type="checkbox" ref={el => this.remember = el as HTMLInputElement} /> Remember Me</label>
			</div>
			<div className="form-group">
				<div className="row">
					<div className="col-sm-6 col-sm-offset-3">
						<input type="submit" className="form-control btn btn-success" value="Log In" onClick={e => this.onLoginClick(e)} />
					</div>
				</div>
			</div>
			<div className="form-group">
				<div className="row">
					<div className="col-lg-12">
						<div className="text-center">
							<a href="#">Forgot Password?</a>
						</div>
					</div>
				</div>
			</div>
		</div>;
	}

	private renderRegister()
	{
		return <div className="col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
			<div className="form-group">
				<input type="email" className={this.getInputStyles(this.state.emailRegister)} name="emailRegister" placeholder="Email Address" value={this.state.emailRegister} onChange={e => this.onInputChange(e)} />
			</div>
			<div className="form-group">
				<input type="password" className={this.getInputStyles(this.state.passwordRegister)} name="passwordRegister" placeholder="Password" value={this.state.passwordRegister} onChange={e => this.onInputChange(e)} />
			</div>
			<div className="form-group">
				<input type="password" className={this.getInputStyles(this.state.passwordConfirm, true)} name="passwordConfirm" placeholder="Confirm Password" value={this.state.passwordConfirm} onChange={e => this.onInputChange(e)} />
			</div>
			<div className="form-group">
				<div className="row">
					<div className="col-sm-6 col-sm-offset-3">
						<input type="submit" className="form-control btn btn-success" value="Register Now" onClick={e => this.onRegisterClick(e)} />
					</div>
				</div>
			</div>
		</div>;
	}

	private onLoginTabClick(e: React.FormEvent<HTMLElement>)
	{
		this.setState({ ...this.state, register: false });
	}

	private onRegisterTabClick(e: React.FormEvent<HTMLElement>)
	{
		this.setState({ ...this.state, register: true });
	}

	public render()
	{
		return <div className="container-fluid">
			<div className="row">
				<div className="col-xs-6 col-xs-offset-3 col-lg-4 col-lg-offset-4">
					<div className="panel panel-login">
						<div className="panel-heading">
							<ul className="nav nav-tabs nav-justified">
								<li>
									<a className={this.state.register ? '' : 'active'} onClick={e => this.onLoginTabClick(e)}>Login</a>
								</li>
								<li>
									<a className={this.state.register ? 'active' : ''} onClick={e => this.onRegisterTabClick(e)}>Register</a>
								</li>
							</ul>
						</div>
						<div className="panel-body">
							<div className="row">
								{this.state.register ? this.renderRegister() : this.renderLogin()}
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>;
	}
}
