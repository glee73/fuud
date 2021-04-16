import React from "react";
import axios from "axios";

function RestaurantListing(props) {
    console.log(props);
    console.log(props.isPinned);
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
    function displayButton() {
        console.log(props.isPinned);
        console.log(props.address);
        if(props.isPinned) {
            return (<button className="alreadyPinned" onClick={sendPinned}>pinned</button>);
        } else {
            return (<button className="pinButton" onClick={sendPinned}>+ pin</button>);
        }
    }
    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.title}</p>
                    {displayButton()}
                    {/*<p className="restaurantTitle">{props.name}</p>*/}
                    {/*<button className="pinButton" onClick={sendPinned}>+ pin</button>*/}
                </div>
                <p className="restaurantAddress">{props.address}</p>
            </div>
        </div>
    );
}

export default RestaurantListing;
