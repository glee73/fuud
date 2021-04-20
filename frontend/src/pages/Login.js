import axios from "axios";
import React from "react";
import {useHistory, Link} from "react-router-dom";
import '../css/index.css';
import graph from '../imgs/gray-graph.gif';
import fuud from '../imgs/fuud.svg';
import {useState} from "react";

function Login() {

    let history = useHistory();
    let [user, setUser] = useState(null);
    let [msg, setMsg] = useState("");

    function incomplete(e) {
        e.preventDefault();
        setMsg(<p className={"error"}> please enter a password </p>);
    }

    function submitLogin(e) {
        e.preventDefault();
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
                if (response.data["success"]) {
                    console.log('success');
                    localStorage.setItem('user', user);
                    setUser(user);
                    return (history.push("/myfeed"));
                } else {
                    console.log('failure');
                    setMsg(<p className={"error"}> {response.data["message"]} </p>);
                }
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    return(
        <div className="login-container">

            <div className={"login-graphics"}>
                <div className={"login-logo"}>
                    <img src={fuud}/>
                </div>

                <div className={"login shadow"}>

                    <form onSubmit={incomplete}>
                        <input type="text" id="username" name="username" placeholder="enter username" required/>
                    </form>

                    <form onSubmit={submitLogin}>
                        <input type="password" id="password" name="password" placeholder="enter password" required/>
                    </form>

                    <button className="submitButton" type="submit" onClick={submitLogin}>log in</button>
                    {msg}
                    <Link className={"link"} to={"/register"}> register </Link>
                </div>
            </div>

            <div className={"graph"}>
                <img src={graph}/>
                <p> discover new restaurants and meet new people along the way! </p>
            </div>

        </div>
    );
}

export default Login;