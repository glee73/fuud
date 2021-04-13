import axios from "axios";
import React from "react";
import {useHistory} from "react-router-dom";
import './index.css';

function Signup() {

    let history = useHistory();

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

        axios.post(
            'http://localhost:4567/signup',
            toSend,
            config
        )
            .then(response => {
                if (!response.data["success"]) {
                    return (<p> {response.data["message"]} </p>);
                } else {
                    return (history.push("/"));
                }
            })
            .catch(function (error) {
                console.log(error);
            });

    }


    return (
        <div className={"register shadow"}>
            <div>
                <form>
                    <input type="text" id="setUsername" name="setUsername" placeholder="choose a username" required/>
                </form>
            </div>
            <div>
                <form>
                    <input type="password" id="setPW" name="setPW" placeholder="choose a password" required/>
                </form>
            </div>
            <div>
                <form>
                    <input type="password" id="confirmPW" name="confirmPW" placeholder="confirm password" required/>
                </form>
            </div>

            <button className="submitButton" type="submit" onClick={submitCreds}>sign up</button>
        </div>
);

}

export default Signup;