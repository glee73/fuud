import axios from "axios";
import React, {useEffect, useState} from "react";
import RestaurantListing from "./RestaurantListing";
import UserListing from "./UserListing";

function TagsGrid(props) {

    let [selected, setSelected] = useState([]);
    let [results, setResults] = useState([]);

    function getResults() {
        const toSend = {
            "tags": selected
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        axios.post(
            'http://localhost:4567/searchbytags',
            toSend,
            config
        )
            .then(response => {
                console.log(response.data["restaurants"]);
                setResults(response.data["restaurants"]);
            })
            .catch(function (error) {
                console.log(error);
            });
    }

    function showResults() {
        if (results === null || results.length === 0) {
            return null;
        }

        let toDisplay = [];

        results.map((result, idx) => (
            toDisplay.push(
                <RestaurantListing
                    name={result.name} address={result.address}
                    key={idx}>
                </RestaurantListing>)
        ));

        return (
            <div id={'restResults'}>
                <p> results: </p>
                <p> {toDisplay} </p>
            </div>
        );
    }


    function addTag(t) {
        setSelected([...selected, t]);
    }

    function showTags() {
        let toDisplay = "Selected: ";

        selected.map((result, idx) => {
            if (idx === 0) {
                toDisplay = toDisplay + result;
            } else {
                toDisplay = toDisplay + ", " + result;
            }
        });
        return (
            <p> {toDisplay} </p>
        )
    }

    return (
        <div>

            <div className="tagsGrid">
                <button className={"tag"} onClick={() => addTag("American")}> American </button>
                <button className={"tag"} onClick={() => addTag("Asian")}> Asian </button>
                <button className={"tag"} onClick={() => addTag("Healthy")}> Healthy </button>
                <button className={"tag"} onClick={() => addTag("Lunch")}> Lunch </button>
                <button className={"tag"} onClick={() => addTag("Dinner")}> Dinner </button>
                <button className={"tag"} onClick={() => addTag("Italian")}> Italian </button>
                <button className={"tag"} onClick={() => addTag("Drinks")}> Drinks  </button>
                <button className={"tag"} onClick={() => addTag("Breakfast")}> Breakfast </button>
                <button className={"tag"} onClick={() => addTag("Mexican")}> Mexican </button>
                <button className={"tag"} onClick={() => addTag("Dessert")}> Dessert </button>
                <button className={"tag"} onClick={() => addTag("Fast Food")}> Fast Food </button>

                {showTags()}

                <button className={"submitButton searchButton"} type={"button"} onClick={getResults}>go</button>
            </div>


            {showResults()}

        </div>
    );
}

export default TagsGrid;