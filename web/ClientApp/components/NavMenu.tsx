import * as React from 'react';
import { Link, NavLink } from 'react-router-dom';

export class NavMenu extends React.Component<{}, {}> {
    public render() {
        return <div className='main-nav'>
                <div className='navbar-inverse flex flex-column'>
                <div className='navbar-header'>
                    <Link className='navbar-brand' to={ '/' }>Smart Calendar</Link>
                </div>
                <div className=''>
                    <ul className='nav'>
						<li>
                            <NavLink to={ '/' } exact activeClassName='active'>
                                <span className='glyphicon glyphicon-calendar'></span> Main
                            </NavLink>
                        </li>
                        <li>
                            <NavLink to={ '/admin' } activeClassName='active'>
                                <span className='glyphicon glyphicon-wrench'></span> Admin
                            </NavLink>
                        </li>
                    </ul>
                </div>
            </div>
        </div>;
    }
}
