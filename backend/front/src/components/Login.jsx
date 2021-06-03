import React, {useState} from "react";
import BackendService from "../services/BackendService";
import Utils from '../utils/Utils'
import history from "../utils/history";
import {connect} from "react-redux";
import {userActions} from "../utils/Rdx";

const Login = (props) => {
    console.log(props, 'props');
    const [state, setState] = useState({
        username: '',
        password: '',
        loggingIn: false,
        submitted: false,
        // errorMessage: null,
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        console.log(e.target, 'HC');
        setState(prevState => ({...prevState, [name]: value}));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setState(prevState => ({...prevState, submitted: true, loggingIn: true}));
        const {username, password} = state;
        try {
            const data = await BackendService.login(username, password);
            if (data.status !== 200) {
                throw new Error('status not 200');
            }
            console.log(data, 'data');
            // Utils.saveUser(data.data);
            props.dispatch(userActions.login(data.data));
            history.push('/home');
        } catch (e) {
            // if (e.response?.status === 401 && e.response) {
            //     setState(prevState => ({...prevState, errorMessage: "Ошибка авторизации"}));
            // } else  {
            //     setState(prevState => ({...prevState, errorMessage: e.message}));
            //
            // }
            setState(prevState => ({...prevState, loggingIn: false}));
            console.error(e);
        }
    };

    const {username, password, loggingIn, submitted} = state;
    console.log('render');
    return (
        <div className="col-md-6 mr-0">
            {state.errorMessage && <div className="alert alert-danger mt-1 mr-0 ml-0" >{state.errorMessage}</div>}
            <h2>Вход</h2>
            <form name="form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Логин</label>
                    <input type="text" className={'form-control' + (submitted && !username ? 'is-invalid' : '')}
                           name="username" value={username}
                           onChange={handleChange}/>
                    {submitted && !username && <div className="help-block text-danger">Введите имя пользователя</div>}
                </div>
                <div className="form-group">
                    <label htmlFor="password">Пароль</label>
                    <input type="password" className={'form-control' + (submitted && !password ? 'is-invalid' : '')}
                           name="password" value={password}
                           onChange={handleChange}/>
                    {submitted && !password && <div className="help-block text-danger">Введите пароль</div>}
                </div>
                <div className="form-group">
                    <button className="btn btn-primary">
                        {loggingIn && <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>}
                        Вход
                    </button>
                </div>
            </form>
        </div>
    );
}

export default connect()(Login);
