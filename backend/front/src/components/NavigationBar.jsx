import React from 'react';
import {connect} from "react-redux";
import {Link, withRouter} from 'react-router-dom';
// import history from "../utils/history";
import {Navbar, Nav, NavDropdown} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faBars, faHome, faUser} from '@fortawesome/free-solid-svg-icons';
import Utils from '../utils/Utils'
import BackendService from "../services/BackendService";
import {userActions} from "../utils/Rdx";

const NavigationBar = ({history, store , ...props}) => {
    const goHome = () => {
     history.push("/home");
    };

    let userName = Utils.getUserName();

    const logout = async () => {
        // Utils.removeUser();
        // goHome();
            BackendService.logout().finally(() => {
                store.dispatch(userActions.logout());
                history.push('/login');
            })

    }

    return (
        <Navbar bg="light" expand="lg">
            <button type='button'
                    className="btn btn-outline-secondary mr-2"
                    onClick={props.toggleSideBar}>
                <FontAwesomeIcon icon={faBars}/>
            </button>
            <Navbar.Brand><Nav.Link as={Link} to="/"><FontAwesomeIcon icon={ faHome }/>{' '}myRP0</Nav.Link></Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <Nav.Link as={Link}  to="/home">Home</Nav.Link>
                    <Nav.Link onClick={goHome}>Another Home</Nav.Link>
                </Nav>
            </Navbar.Collapse>
            <Navbar.Text>{userName}</Navbar.Text>
            {userName && <Nav.Link onClick={logout}><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Logout</Nav.Link>}
            {!userName && <Nav.Link as={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth />{' '}Login</Nav.Link>}
        </Navbar>
    );
}

let mapStateToProps = (state) => {
    return {
        user: state.authentication,
    }
}

export default connect(mapStateToProps, {})(withRouter(NavigationBar));
