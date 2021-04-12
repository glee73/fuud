import NewPost from './NewPost';
import axios from "axios";
import React, {useEffect, useState} from "react";

function Feed() {

        const userName = "ethan";

        let [posts, setPosts] = useState([]);

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
                        console.log(response.data["feed"])
                        setPosts(response.data["feed"]);
                        return response.data["feed"];
                    })
                    .catch(function (error) {
                            console.log(error);
                    });
        }


        useEffect(() => {
            getFeedPosts();
        })


        const displayPosts = () => {
            let content = [];

            posts.map((post, idx) => (
                content.push(
                    <NewPost className={"profileItem"} key={idx}
                             user={userName} rating={post.reviewOutOfTen}
                             desc={post.description}> </NewPost>)
            ));

            return (<div className="profileGrid">
                    {content}
                </div>
            );
        }


        return (
            <div className="feed">
                <p className="feedTitle pageTitle">what your friends are saying</p>
                {displayPosts()}
            </div>
        );
}

export default Feed;

