import React from "react";
import axios from "axios";

function RestaurantListing(props) {
    function sendPinned() {
        const toSend = {
            "username": props.user,
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
                console.log(true);
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.name}</p>
                    <button className="pinButton" onClick={sendPinned}>+ pin</button>
                </div>
                <p className="restaurantAddress">{props.address}</p>
            </div>
        </div>
    );
}

export default RestaurantListing;
