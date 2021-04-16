import RestaurantListing from "../components/RestaurantListing";
import Navbar from "../components/Navbar.js"
import axios from "axios";
import React, {useEffect, useState} from "react";

function Recommendations(props) {
    const userName = props.user;
    console.log("username" + userName);
    let [recommendations, setRecommendations] = useState(null);
    let [pinned, setPinned] = useState(false);
    useEffect(() => {
        getRecommendedRestaurants();
    }, []);
    function getRecommendedRestaurants() {
        console.log("getting recommendations");
        const toSend = {
            "username": userName
        };
        console.log("1");
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };
        console.log("2");
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
    // function checkIfPinned(restID) {
    //     const toSend = {
    //         "username": userName,
    //         "restID": restID
    //     };
    //     let config = {
    //         headers: {
    //             "Content-Type": "application/json",
    //             'Access-Control-Allow-Origin': '*',
    //         }
    //     };
    //     axios.post(
    //         'http://localhost:4567/checkpin',
    //         toSend,
    //         config
    //     )
    //         .then(response => {
    //             console.log(response.data["ispinned"]);
    //             setPinned(response.data["ispinned"])
    //             return response.data["ispinned"];
    //         })
    //         .catch(function (error) {
    //             console.log(error);
    //         });
    // }


    const displayRecommendations = () => {
        console.log("data: " + recommendations);
        let content = [];
        if (recommendations === null) {
            return <p>Getting your recommendations ...  </p>
        }
        recommendations.map((rec, idx) => (
            content.push(
                <RestaurantListing className={"recommendation"} address={rec.address}
                                   key={idx} restID={rec.id} title={rec.name} user={userName}></RestaurantListing>
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
