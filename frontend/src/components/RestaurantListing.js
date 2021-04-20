import PinButton from "./PinButton";
import React, {useState, useEffect} from "react";

function RestaurantListing(props) {

    let [pinned, setPinned] = useState(props.isPinned);

    function updatePinned(status) {
        setPinned(status);
    }

    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.name}</p>
                    <PinButton isPinned={pinned} update={updatePinned} restID={props.restID}/>
                </div>
                <p className="restaurantAddress">{props.address}</p>
            </div>
        </div>
    );
}

export default RestaurantListing;
