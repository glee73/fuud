import axios from "axios";
import React, {useEffect, useState} from "react";

function TagsGrid(props) {

    let [selected, setSelected] = useState([]);

    return (
        <div className="postContainer restaurantContainer shadow">
            <div className="restaurantContent">
                <div className="pinFlex">
                    <p className="restaurantTitle">{props.name}</p>
                    <button className="pinButton" onClick={sendPinned}>+ pin</button>
                </div>
                <p className="restaurantAddress">{props.address}</p>
            </div>
        </div>
    );
}

export default TagsGrid;
