import React from "react";
import Navbar from "./Navbar";

function Loading(props) {

    return (
        <div className={"loading-page"}>
            <div className="restaurant">
                <div className={"faded"}>
                    {props.text}
                </div>
                <div className={"loading"}>
                    <img src={"https://media.giphy.com/media/KfxPgR9Xb6lRvlFa8x/giphy.gif"} alt={"dancing taco loading page"}/>
                    <p id={"dots"}>loading</p>
                </div>
            </div>
        </div>

    )
}

export default Loading;