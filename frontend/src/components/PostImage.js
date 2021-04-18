import React from "react";

function PostImage(props) {
    return (<img className={"postImage"} src={props.data} alt={"post image"}/>);
}

export default PostImage;