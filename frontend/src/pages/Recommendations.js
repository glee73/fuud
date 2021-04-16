import RestaurantListing from "../components/RestaurantListing";
import Navbar from "../components/Navbar.js"
import axios from "axios";
import React, {useEffect, useState} from "react";

function Recommendations(props) {
    const userName = localStorage.getItem("user");
    let [recommendations, setRecommendations] = useState(null);

    let [pinned, setPinned] = useState(null);

    useEffect(() => {
        props.getUser();
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
                console.log("recs: " + response.data["recommended"]);
                console.log("bools: " + response.data["bools"]);
                // recommendations = response.data["recommended"];
                console.log("recc array: " + recommendations);
                setRecommendations(response.data["recommended"]);
                setPinned(response.data["bools"]);
                // pinned = response.data["bools"];
                console.log("pinned array " + pinned);
                return response.data["recommended"];
            })
            .catch(function (error) {
                console.log(error);
            });
    }
    const displayRecommendations = () => {
        console.log("data: " + recommendations);
        if (recommendations === null || recommendations === "undefined") {
            return <p>Getting your recommendations ...  </p>
        } else {
            let content = [];
            console.log("pinned bools: " + pinned);
            // if (recommendations === null) {
            //     return <p>Getting your recommendations ...  </p>
            // }
            recommendations.map((rec, idx) => (
                content.push(
                    <RestaurantListing className={"recommendation"} address={rec.address}
                                       key={idx} restID={rec.id} name={rec.name} user={userName} isPinned={pinned[idx]}/>
                )
            ));
            return (<div className="recommendationsDisplay">
                {content}
            </div>)
        }

    }
    // props.redirect();
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
