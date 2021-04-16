import React from "react";
import axios from "axios";

function RestaurantListing(props) {
    const userName = localStorage.getItem("user");
    console.log("in rest listing: " + props.isPinned);
    function sendPinned() {
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
                console.log(true);
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    function sendUnpin() {
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
                console.log(response.data["success"]);
            })
            .catch(function (error) {
                console.log(error);
            });
        window.location.reload(false);
    }
    function displayButton() {
        if(!props.isPinned) {
            console.log("should be true");
            return (<button className="alreadyPinned" onClick={sendUnpin}>pinned</button>);
        } else {
            console.log("should be false");
            return (<button className="pinButton" onClick={sendPinned}>+ pin</button>);
        }
    }
    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.name}</p>
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
