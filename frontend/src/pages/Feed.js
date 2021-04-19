import Post from '../components/Post';
import axios from "axios";
import React, {useEffect, useState} from "react";
import '../css/index.css';
import Navbar from "../components/Navbar";

function Feed(props) {

        let userName = localStorage.getItem("user");

        let [posts, setPosts] = useState(null);
        let [pics, setPics] = useState(null);

        function getFeedPosts() {
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
                    'http://localhost:4567/feed',
                    toSend,
                    config
                )
                    .then(response => {
                        console.log(response.data["usernameToPic"])
                        setPosts(response.data["feed"]);
                        setPics(response.data["usernameToPic"]["nameValuePairs"]);
                        return response.data["feed"];
                    })
                    .catch(function (error) {
                            console.log(error);
                    });
        }


        useEffect(() => {
            props.getUser();
            getFeedPosts();
        }, [])


        const displayPosts = () => {
            let content = [];

            if (posts.length === 0) {
                return <p> No posts to show. Find other users to follow by entering their username in the search bar above!</p>
            }

            posts.map((post, idx) => (
                content.push(
                    <Post className={"profileItem"} key={idx}
                          user={post.user} rating={post.reviewOutOfTen}
                          desc={post.description} time={post.timestamp}
                          resID={post.restaurantID} pic={post.pic}
                          profPic={pics[post.user]} postID={post.id}>
                    </Post>)
            ));

            return (<div className="profileGrid">
                    {content}
                </div>
            );
        }


        if (posts === null || pics === null) {
            return (
                <div>
                    <Navbar logout={props.logout}/>
                </div>
            );
        } else {
            return (
                <div>
                    <Navbar logout={props.logout}/>
                    <div className="feed">
                        <p className="feedTitle pageTitle">what your friends are saying</p>
                        {displayPosts()}
                    </div>
                </div>
            );
        }
}

export default Feed;

