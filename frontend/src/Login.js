import axios from "axios";
import React, {useState} from "react";
import { Redirect } from "react-router-dom";

function Login() {

    let [status, setStatus] = useState(false);
    let [msg, setMsg] = useState("");

    function submitLogin() {
        let user = document.getElementById('username');
        let pw = document.getElementById('password');

        if (user == null || pw == null) {
            return;
        } else {
            user = user.value;
            pw = pw.value;
        }

        const toSend = {
            "username": user,
            "password": pw
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/login',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["success"]);
                setStatus(response.data["success"]);
                setMsg(response.data["message"]);
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    function onSuccess() {
        submitLogin();
        if (!status) {
            return (<p> {msg} </p>);
        } else {
            return (<Redirect to={"/explore"} />)
        }
    }


    return(
        <div className="login">

            <p> sign in here! </p>

            <form className="">
                <label htmlFor="username">Username:</label>
                <input type="text" id="username" name="username" required/>
            </form>

            <form className="">
                <label htmlFor="password">Password:</label>
                <input type="password" id="password" name="password" required/>
            </form>

            <button className="submitButton" type="submit" onClick={onSuccess}>log in</button>
        </div>
    );
}

export default Login;