import * as React from "react";

export interface IConfirmPopupProps
{
	onClose: (isConfirmed: boolean) => void;
}

export interface IConfirmPopupState
{

}

export class ConfirmPopup extends React.Component<IConfirmPopupProps, IConfirmPopupState> {

	constructor(props: IConfirmPopupProps)
	{
		super(props);

		this.state = {
		};
	}

	renderPopup()
	{
		return (<div className="modal">
			        <div className="modal-dialog">
				        <div className="modal-content">
					        <div className="modal-header">
						        <h4 className="modal-title" id="myModalLabel">Do you wish to delete this event?</h4>
					        </div>
					        <div className="modal-footer">
						        <button type="button" className="btn btn-danger" onClick={() => this.props.onClose(true)}>Delete</button>
								<button type="button" className="btn btn-default" onClick={() => this.props.onClose(false)}>Cancel</button>
					        </div>
				        </div>
			        </div>
		        </div>);
	}

	render()
	{
		return this.renderPopup();
	}
}
