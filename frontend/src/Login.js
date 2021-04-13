import axios from "axios";
import React from "react";
import {useHistory, Link} from "react-router-dom";
import './index.css';
import graph from './gray-graph.gif';
import fuud from './fuud.svg';
import {useState, useEffect} from "react";

function Login(props) {

    let history = useHistory();
    let [user, setUser] = useState(null);
    let [msg, setMsg] = useState("");
    let [res, setRes] = useState(false);

    useEffect(() => {
        props.getUser(user);
    },[user]);

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
                setMsg(response.data["message"]);
                setRes(response.data["success"])
                console.log(response.data["success"], response.data["message"]);
            })
            .catch(function (error) {
                console.log(error);
            });

    }

    function afterSubmit() {
        if (!res) {
            return <p className={"error"}> {msg} </p>
        } else {
            setUser(user);
            return (history.push("/explore"));
        }
    }

    return(
        <div className="login-container">

            <div className={"login-graphics"}>
                <div className={"login-logo"}>
                    <img src={fuud}/>
                </div>

                <div className={"login shadow"}>

                    <form>
                        <input type="text" id="username" name="username" placeholder="enter username" required/>
                    </form>

                    <form>
                        <input type="password" id="password" name="password" placeholder="enter password" required/>
                    </form>

                    <button className="submitButton" type="submit" onClick={submitLogin}>log in</button>
                    {afterSubmit()}
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