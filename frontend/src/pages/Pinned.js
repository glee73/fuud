import Navbar from "../components/Navbar.js";
import axios from "axios";
import React, {useEffect, useState} from "react";
import RestaurantListing from "../components/RestaurantListing";
import Loading from "../components/Loading";

function Pinned(props) {
    const userName = localStorage.getItem("user");
    console.log(userName);

    let [pinned, setPinned] = useState(null);

    useEffect(() => {
        props.getUser();
        getPinned();
    }, [])

    function getPinned() {
        console.log("getting pinned");
        const toSend = {
            "username": userName
        };
        console.log(toSend);
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
        console.log(pinned);
        let content = [];
        if (pinned.length === 0) {
            return <p>No pinned restaurants. Find restaurants you want to save through the recommended and explore page!</p>
        }
        pinned.map((rest, idx) => (
            content.push(
                <RestaurantListing user={userName} restID={rest.id} className={"recommendation"} address={rest.address}
                                   key={idx} name={rest.name} isPinned={true}/>
            )
        ));
        return (<div className="recommendationsDisplay">
            {content}
        </div>)
    }

    if (pinned === null) {
        return (
            <div>
                <Navbar logout={props.logout} page={"mypinned"}/>
                <Loading text={<p className="pinnedTitle pageTitle">your
                    pinned</p>}/>
            </div>
        );
    }
    return (
            <div>
        <Navbar logout = {props.logout} page={"mypinned"}/>
        <div className="restaurant">
            <p className="pinnedTitle pageTitle">your pinned</p>
            {displayPinned()}
        </div>
    </div>

    );


}

export default Pinned;