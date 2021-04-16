import RestaurantListing from "../components/RestaurantListing";
import Navbar from "../components/Navbar.js"
import axios from "axios";
import React, {useEffect, useState} from "react";

function Recommendations(props) {
    const userName = props.user;
    let [recommendations, setRecommendations] = useState(null);
    useEffect(() => {
        getRecommendedRestaurants();
    }, []);
    function getRecommendedRestaurants() {
        console.log("getting recommendations");
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
            'http://localhost:4567/recommended',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["recommended"]);
                setRecommendations(response.data["recommended"]);
                return response.data["recommended"];
            })
            .catch(function (error) {
                console.log(error);
            });
    }


    const displayRecommendations = () => {
        console.log("data: " + recommendations);
        let content = [];
        if (recommendations === null) {
            return <p>Getting your recommendations ...  </p>
        }
        recommendations.map((rec, idx) => (
            content.push(
                <RestaurantListing className={"recommendation"} address={rec.address}
    key={idx} restID={rec.id} title={rec.name} user={userName}/>
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
                <p className="restaurantTitle pageTitle">your recommendations</p>
                {displayRecommendations()}
            </div>
        </div>

    )
        ;
}

export default Recommendations;
