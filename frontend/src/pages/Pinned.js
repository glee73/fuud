import Navbar from "../components/Navbar.js";
import axios from "axios";
import React, {useEffect, useState} from "react";
import RestaurantListing from "../components/RestaurantListing";

function Pinned(props) {
    const userName = props.user;
    let [pinned, setPinned] = useState(null);
    useEffect(() => {
        getPinned();
    }, [])
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
            'http://localhost:4567/getpinned',
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
    const displayPinned = () => {
        console.log("data: " + pinned);
        let content = [];
        if (pinned === null) {
            return <p>Getting your pinned restaurants ...  </p>
        }
        pinned.map((rest, idx) => (
            content.push(
                <RestaurantListing className={"recommendation"} address={rest.address}
                                   key={idx} title={rest.name}></RestaurantListing>
            )
        ));
        return (<div className="recommendationsDisplay">
            {content}
        </div>)

    }
    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="restaurant">
                <p className="pinnedTitle pageTitle">your pinned</p>
                {displayPinned()}
            </div>
        </div>

    );


}

export default Pinned;