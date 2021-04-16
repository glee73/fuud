import React from "react";

function RestaurantListing(props) {
    return (
        <div className="postContainer restaurantContainer shadow">
            {/*<img className="restaurantImage" src={props.imgURL} />*/}
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.title}</p>
                    <button className="pinButton">+ pin</button>
                </div>
                <p className="restaurantAddress">{props.address}</p>
            </div>
        </div>
    );
}

export default RestaurantListing;
