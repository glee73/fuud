import React, {useEffect, useState} from "react";
import axios from "axios";

function ProfilePic(props) {
    if (props.data === null || props.data === "none" || props.data === 'undefined') {
        return <div className={"profilePic greyCircle"}/>
    } else {
        return (<img className={"profilePic"} src={props.data} alt={"profile" +
        " picture of user"}/>);
    }


}

export default ProfilePic;
