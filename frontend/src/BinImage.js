import React from "react";
function BinImage(props) {
    return (<img src={`data:image/jpeg;base64,${props.data}`}/>);
}

export default BinImage;