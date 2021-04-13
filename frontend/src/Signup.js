import axios from "axios";
import React, {useEffect, useState} from "react";
import {Redirect} from "react-router-dom";
import './index.css';

function Signup() {

    let [status, setStatus] = useState(false);
    let [msg, setMsg] = useState("");

    function submitCreds() {
        let user = document.getElementById('setUsername');
        let pw = document.getElementById('setPW');
        let confirm = document.getElementById('confirmPW');

        if (user == null || pw == null || confirm == null) {
            return;
        } else {
            user = user.value;
            pw = pw.value;
            confirm = confirm.value;
        }

        const toSend = {
            "username": user,
            "password": pw,
            "password2": confirm
        };


        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        console.log(toSend);

        axios.post(
            'http://localhost:4567/signup',
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
        submitCreds();
        if (!status) {
            return (<p> {msg} </p>);
        } else {
            return (<Redirect to={"/login"} />)
        }
    }

    console.log(msg);




    return (
        <div>
            <div className="">
                <form className="">
                    <label htmlFor="setUsername">Choose a username:</label>
                    <input className={"shadow"} type="text" id="setUsername" name="setUsername" required/>
                </form>
            </div>
            <div className="">
                <form className="">
                    <label htmlFor="setPW">Choose a password:</label>
                    <input className={"shadow"} type="password" id="setPW" name="setPW" required/>
                </form>
            </div>
            <div className="">
                <form className="">
                    <label htmlFor="confirmPW">Confirm password:</label>
                    <input className={"shadow"} type="password" id="confirmPW" name="confirmPW" required/>
                </form>
            </div>

            <button className="submitButton" type="submit" onClick={onSuccess}>sign up</button>
        </div>
);

}

export default Signup;