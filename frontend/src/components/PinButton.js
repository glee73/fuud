import React, {useEffect, useState} from "react";
import axios from "axios";

function PinButton(props) {

    const userName = localStorage.getItem("user");
    let [pinned, setPinned] = useState(props.isPinned);

    useEffect(() => {
        props.update();
    }, [pinned]);

    console.log("in rest listing: " + props.isPinned);

    function pin() {
        const toSend = {
            "username": userName,
            "restID": props.restID
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.post(
            'http://localhost:4567/addpin',
            toSend,
            config
        )
            .then(response => {
                if (response.data["success"]) {
                    setPinned(true);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function unpin() {
        const toSend = {
            "username": userName,
            "restID": props.restID
        };
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };
        axios.post(
            'http://localhost:4567/unpin',
            toSend,
            config
        )
            .then(response => {
                if (response.data["success"]) {
                    setPinned(false);
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }


    if (pinned) {
        return (<button className="alreadyPinned" onClick={unpin}>pinned</button>);
    } else {
        return (<button className="pinButton" onClick={pin}>+ pin</button>);
    }
}

export default PinButton;