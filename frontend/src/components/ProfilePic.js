import React, {useEffect, useState} from "react";
import axios from "axios";

function ProfilePic(props) {

    let [path, setPath] = useState(props.data);

    useEffect(() => {
        setPath(props.data);
    }, [props.data])

    console.log(props.bigger)

    if (props.bigger !== undefined) {
        if (path === null || path === 'none' || path === undefined) {
            return (<img className={"profilePic avatar"} src={"https://img.icons8.com/plumpy/96/000000/user-male-circle.png"} alt={"profile" +
                " picture of poster"}/>)
        } else {
            return (<img className={"profilePic avatar"} src={path} alt={"profile" +
            " picture of poster"}/>);
        }
    }

    if (path === null || path === 'none' || path === undefined) {
        return (<img className={"profilePic"} src={"https://img.icons8.com/plumpy/96/000000/user-male-circle.png"} alt={"profile" +
        " picture of poster"}/>)
    } else {
        return (<img className={"profilePic"} src={path} alt={"profile" +
        " picture of poster"}/>);
    }


}

export default ProfilePic;
