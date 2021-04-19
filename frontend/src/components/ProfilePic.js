import React, {useEffect, useState} from "react";
import axios from "axios";

function ProfilePic(props) {

    let [path, setPath] = useState(props.data);

    useEffect(() => {
        setPath(props.data);
    }, [props.data])

    if (path === null) {
        return <div className={"profilePic greyCircle"}/>
    } else {
        return (<img className={"profilePic"} src={path} alt={"profile" +
        " picture of poster"}/>);
    }


}

export default ProfilePic;
