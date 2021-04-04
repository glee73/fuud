import './App.css';
import { AwesomeButton } from "react-awesome-button";
import React, {useEffect, useState} from 'react';
import axios from "axios";

function CheckIn() {

    const [log, setLog] = useState("");
    const [userInfo, setUserInfo] = useState("");
    const [userName, setUserName] = useState("");

    /**
     * Makes an axios request to get log of all users.
     */
    const reqLog = () => {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.get(
            "http://localhost:4567/checkin",
            config
        )
            .then(response => {
                console.log(response.data["checkin"]);
                // setLog(response.data["checkin"]);
                convertToButtons(response.data["checkin"]);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    /**
     * Makes an axios request to get specific user's info.
     */
    const reqUserInfo = (usernameInput) => {
        const toSend = {
            username: usernameInput
        };

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(
            "http://localhost:4567/userdata",
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["userdata"]);
                setUserInfo(response.data["userdata"]);
                setUserName("");
                // setLog(response.data["checkin"]);
                // convertToButtons(response.data["checkin"]);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    /**
     * Method that maps each data point in log to a button.
     * @param passedLog (all user data)
     */
    const convertToButtons = (passedLog) => {
            setLog(passedLog.map((passedLog) => <AwesomeButton type="primary" onPress={() => reqUserInfo(passedLog[1])}>{passedLog[0]}</AwesomeButton>));
    }

    const handleChange = (event) => {setUserName(event.target.value)}

    /**
     * initialization.
     */
    useEffect(() => {
        reqLog();
    }, []);

    /**
     * constantly updates log.
     */
    useEffect(() => {
        reqLog();
    }, [log]);

    return (
        <div>
            <div id = "log" className="scroll-box1">
                {log}
            </div>
            <div id = "userInfo" className="scroll-box2">
                {userInfo}
            </div>
            <div>
                <label> Request User Data </label>
                <input type={'text'} onChange = {handleChange}/>
                <AwesomeButton onPress={() => reqUserInfo(userName)}> Go </AwesomeButton>
            </div>
        </div>

    );

}

export default CheckIn;
