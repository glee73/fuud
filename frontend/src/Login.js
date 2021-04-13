import axios from "axios";
import React, {useState} from "react";
import {useHistory, Link} from "react-router-dom";
import './index.css';
import graph from './gray-graph.gif';
import fuud from './fuud.svg';

function Login() {

    let [status, setStatus] = useState(false);
    let [msg, setMsg] = useState("");

    let history = useHistory();

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
                console.log(response.data["success"], response.data["message"]);
                setStatus(response.data["success"]);
                setMsg(response.data["message"]);

                if (!response.data["success"]) {
                    return (<p> {response.data["message"]} </p>);
                } else {
                    return (history.push("/explore"))
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

                    <form className="">
                        <input type="text" id="username" name="username" placeholder="enter username" required/>
                    </form>

                    <form className="">
                        <input type="password" id="password" name="password" placeholder="enter password" required/>
                    </form>

                    <button className="submitButton" type="submit" onClick={submitLogin}>log in</button>
                    <Link className={"link"} to={"/signup"}> register </Link>
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