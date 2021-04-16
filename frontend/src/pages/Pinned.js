import Navbar from "../components/Navbar.js";
import axios from "axios";
import React, {useEffect, useState} from "react";

function Pinned(props) {
    const userName = props.user;
    let [pinned, setPinned] = useState(null);
    function getPinned() {
        console.log("getting pinned");
        const toSend = {
            "username": userName
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.post(
            'http://localhost:4567/mypinned',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["pinned"]);
                setPinned(response.data["pinned"]);
                return response.data["pinned"];
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="restaurant">
                <p className="pinnedTitle pageTitle">your pinned</p>
            </div>
        </div>

    );


}

export default Pinned;