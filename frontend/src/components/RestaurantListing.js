import React from "react";

function RestaurantListing(props) {
    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <p className="restaurantTitle">{props.name}</p>
                <p className="restaurantDesc">{props.address}</p>
            </div>
        </div>
    );
}

export default RestaurantListing;
