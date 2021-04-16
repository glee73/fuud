import Navbar from "../components/Navbar";
import React, {useEffect, useState} from "react";
import axios from "axios";
import UserListing from "../components/UserListing";
import RestaurantListing from "../components/RestaurantListing";
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';
import TagsGrid from "../components/TagsGrid";

function Explore(props) {

    const userName = localStorage.getItem("user");

    useEffect(() => {
        props.getUser();
    }, []);

    let [results, setResults] = useState(null);
    let userResults = null;
    let restResults = null;
    let [activeIndex, setActiveIndex] = useState(0);

    
    function searchForUser() {
        setActiveIndex(0);
        setResults(null);
    }

    function searchForRest() {
        setActiveIndex(1);
        setResults(null);
    }

    function searchByTag() {
        setActiveIndex(2);
        setResults(null);
    }

    function getQuery() {
        if (activeIndex === 0) {
            console.log(document.getElementById('user-query'))
            return document.getElementById('user-query');
        } else if (activeIndex === 1) {
            return document.getElementById('rest-query');
        }
    }

    function getResults() {
        let query = getQuery();

        if (query === null) {
            return;
        } else {
            query = getQuery().value;
        }

        const toSend = {
            "query": query
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        };

        if (activeIndex === 0) {
            axios.post(
                'http://localhost:4567/searchforuser',
                toSend,
                config
            )
                .then(response => {
                    userResults = response.data["user"];
                    setResults(response.data["user"]);
                    return response.data["user"];
                })
                .catch(function (error) {
                    console.log(error);
                });

        } else if (activeIndex === 1) {
            axios.post(
                'http://localhost:4567/searchforrestaurants',
                toSend,
                config
            )
                .then(response => {
                    restResults = response.data["restaurant"];
                    setResults(response.data["restaurant"]);
                    return response.data["restaurant"];
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }

    function displayRestResults() {

        if (results === null || activeIndex !== 1) {
            return null;
        }

        if (results.length === 0) {
            return (
                <div>
                    <p> results: </p>
                    <p> your search turned up empty! try another query </p>
                </div>
            );
        } else {
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

    }

    function displayUserResults() {

        if (results === null || activeIndex !== 0) {
            return null;
        }

        if (results.length === 0) {
            return (
                <div>
                    <p> results: </p>
                    <p> your search turned up empty! try another query </p>
                </div>
            );
        } else {
            let toDisplay = [];

            toDisplay.push(
                <UserListing searchedUser={results[0]} currUser={userName}/>
            )


            return (
                <div id={'userResults'}>
                    <p> results: </p>
                    <p> {toDisplay} </p>
                </div>
            );
        }
    }

    function displayResults() {
        console.log(activeIndex)
        if (activeIndex === 0) {
            console.log(displayUserResults());
            return displayUserResults();
        } else if (activeIndex === 1) {
            console.log("restaurant call");
            return displayRestResults();
        } else {
            // display tag shit
        }
    }



    return (
        <div>
            <Navbar logout={props.logout}/>

            <div className={"explorePage"}>
                <p className={"pageTitle"}> Looking for something? </p>

                <Tabs className={"tabs"}>
                    <TabList>
                        <Tab onClick={() => searchForUser()}>a user</Tab>
                        <Tab onClick={() => searchForRest()}>a restaurant</Tab>
                        <Tab onClick={() => searchByTag()}>restaurants by tag</Tab>
                    </TabList>

                    <TabPanel>
                        <div>
                            <form>
                                <input id={"user-query"} className={"searchBar" +
                                " shadow"} type={"text"} placeholder={"search for a user by username"}/>
                                <button className={"submitButton searchButton"} type={"button"} onClick={getResults}>search</button>
                            </form>
                            {displayResults()}
                        </div>
                    </TabPanel>

                    <TabPanel>
                        <form>
                            <input id={"rest-query"} className={"searchBar shadow"} type={"text"} placeholder={"search for a restaurant by restaurant name"}/>
                            <button className={"submitButton searchButton"} type={"button"} onClick={getResults}>search</button>
                        </form>
                        {displayResults()}
                    </TabPanel>

                    <TabPanel>
                        select which tags you want to explore
                        <TagsGrid/>


                    </TabPanel>
                </Tabs>
            </div>
        </div>
    );

}

export default Explore;