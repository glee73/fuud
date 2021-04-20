import PinButton from "./PinButton";
import React, {useState, useEffect} from "react";

function RestaurantListing(props) {

    let [pinned, setPinned] = useState(props.isPinned);
    console.log("in rest listing: " + props.isPinned);

    useEffect(() => {
        setPinned(props.isPinned)
    }, [props.restID])


    function updatePinned(status) {
        if (status) {
            setPinned(true);
        } else {
            setPinned(false);
        }
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
