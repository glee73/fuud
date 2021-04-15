import RestaurantListing from "../components/RestaurantListing";
import Navbar from "../components/Navbar.js"
import React from "react";

function RestaurantSearch(props) {

    let userName = props.user;

    return (
        <div>
            <Navbar logout={props.logout}/>
            <div className="restaurant">
                <p className="restaurantTitle pageTitle">search results</p>
                <RestaurantListing title="Kung Fu Tea" imgURL="https://d1ralsognjng37.cloudfront.net/281ebbce-1a1d-495d-9b6f-fef70842acb9.jpeg"></RestaurantListing>
                <RestaurantListing title="Flatbreaad" imgURL="https://www.peta.org/wp-content/uploads/2016/01/by-CHLOE-pic.jpg"></RestaurantListing>
            </div>
        </div>

    )
        ;
}

export default RestaurantSearch;
